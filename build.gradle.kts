import de.undercouch.gradle.tasks.download.Download
import org.jetbrains.intellij.tasks.PrepareSandboxTask
import java.security.MessageDigest
import java.util.zip.ZipFile
import kotlin.io.path.deleteExisting

plugins {
  id("java")
  alias(libs.plugins.changelog)
  alias(libs.plugins.download)
  alias(libs.plugins.grammarkit)
  alias(libs.plugins.intellij)
  alias(libs.plugins.kotlin)
}

intellij {
  type.set("IC")
  version.set(libs.versions.intellij)
  plugins.set(listOf("org.intellij.intelliLang", "terminal"))
  pluginName.set("PowerShell")
}

sourceSets {
  main {
    java.srcDir("src/main/gen-parser")
    java.srcDir("src/main/gen-lexer")
    resources {
      exclude("**.bnf")
      exclude("**.flex")
    }
  }
}

group = "com.intellij.plugin"
version = "2.5.0"

repositories {
  mavenCentral()
  maven {
    setUrl("https://mvnrepository.com/artifact/com.kohlschutter.junixsocket/junixsocket-common")
  }
}

dependencies {
  implementation(libs.bundles.junixsocket)

  implementation(libs.lsp4j)
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
  testImplementation(libs.junit)
}

configurations {
  runtimeClasspath {
    // NOTE: Newer versions of these libraries are provided by IntelliJ, so let's exclude them from the dependency set
    // of org.eclipse.lsp4j:
    exclude("com.google.code.gson", "gson")
    exclude("com.google.guava", "guava")
  }
}

tasks {
  wrapper {
    gradleVersion = "8.3"
  }

  val resources = file("src/main/resources")

  generateLexer {
    val genLexerRoot = file("src/main/gen-lexer")
    val genLexerPackageDirectory = genLexerRoot.resolve("com/intellij/plugin/powershell/lang")

    purgeOldFiles = true
    sourceFile = resources.resolve("_PowerShellLexer.flex")
    targetOutputDir = genLexerPackageDirectory
    defaultCharacterEncoding = "UTF-8"
  }

  generateParser {
    val genParserRoot = file("src/main/gen-parser")

    purgeOldFiles = true
    sourceFile = resources.resolve("PowerShell.bnf")
    targetRootOutputDir = genParserRoot
    pathToParser = "com/intellij/plugin/powershell/lang/parser"
    pathToPsiRoot = "com/intellij/plugin/powershell/psi"
    defaultCharacterEncoding = "UTF-8"
  }

  withType<JavaCompile> {
    dependsOn(generateLexer, generateParser)

    options.encoding = "UTF-8"
    sourceCompatibility = "17"
    targetCompatibility = "17"
  }
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    dependsOn(generateLexer, generateParser)

    kotlinOptions.jvmTarget = "17"
  }

  val downloads = layout.buildDirectory.get().dir("download")

  val psScriptAnalyzerVersion: String by project
  val psScriptAnalyzerSha256Hash: String by project

  val psScriptAnalyzerFileName = "PSScriptAnalyzer.$psScriptAnalyzerVersion.nupkg"
  val psScriptAnalyzerOutFile = downloads.file(psScriptAnalyzerFileName)

  val downloadPsScriptAnalyzer by register<Download>("downloadPsScriptAnalyzer") {
    group = "dependencies"

    inputs.property("version", psScriptAnalyzerVersion)
    inputs.property("hash", psScriptAnalyzerSha256Hash)

    // NOTE: Do not overwrite: the verification step should delete an incorrect file.
    // NOTE: Notably, this property allows us to skip the task completely if no inputs change.
    overwrite(false)

    src("https://github.com/PowerShell/PSScriptAnalyzer/releases/download/" +
      "$psScriptAnalyzerVersion/$psScriptAnalyzerFileName")
    dest(psScriptAnalyzerOutFile)

    doLast {
      val data = psScriptAnalyzerOutFile.asFile.readBytes()
      val hash = MessageDigest.getInstance("SHA-256").let { sha256 ->
        sha256.update(data)
        sha256.digest().joinToString("") { "%02x".format(it) }
      }
      if (!hash.equals(psScriptAnalyzerSha256Hash, ignoreCase = true)) {
        psScriptAnalyzerOutFile.asFile.toPath().deleteExisting()
        error("PSScriptAnalyzer hash check failed. Expected ${psScriptAnalyzerSha256Hash}, but got $hash\n" +
          "Please try running the task again, or update the expected hash in the gradle.properties file.")
      }
    }
  }

  val getPsScriptAnalyzer by registering(Copy::class) {
    group = "dependencies"

    val outDir = projectDir.resolve("language_host/current/LanguageHost/modules/PSScriptAnalyzer")
    doFirst {
      if (!outDir.deleteRecursively()) error("Cannot delete \"$outDir\".")
    }

    dependsOn(downloadPsScriptAnalyzer)
    from(zipTree(psScriptAnalyzerOutFile))
    // NuGet stuff:
    exclude("_manifest/**", "_rels/**", "package/**", "[Content_Types].xml", "*.nuspec")

    // Compatibility profiles, see https://github.com/PowerShell/PSScriptAnalyzer/issues/1148
    exclude("compatibility_profiles/**")
    into(outDir)
  }

  withType<PrepareSandboxTask> {
    dependsOn(getPsScriptAnalyzer)
    from("${project.rootDir}/language_host/current") {
      into("${intellij.pluginName.get()}/lib/")
    }
  }

  val maxUnpackedPluginBytes: String by project
  val verifyDistributionSize by registering {
    group = "verification"
    dependsOn(buildPlugin)

    doLast {
      val artifact = buildPlugin.flatMap { it.archiveFile }.get().asFile
      val unpackedSize = ZipFile(artifact).use { it.entries().asSequence().sumOf { e -> e.size } }
      val unpackedSizeMiB = "%.3f".format(unpackedSize / 1024.0 / 1024.0)
      if (unpackedSize > maxUnpackedPluginBytes.toLong()) {
        error(
          "The resulting artifact size is too large. Expected no more than $maxUnpackedPluginBytes, but got" +
            " $unpackedSize bytes ($unpackedSizeMiB MiB).\nArtifact path: \"$artifact\"."
        )
      }

      println("Verified unpacked distribution size: $unpackedSizeMiB MiB.")
    }
  }

  check {
    dependsOn(verifyDistributionSize)
  }

  runIde {
    jvmArgs("-Dide.plugins.snapshot.on.unload.fail=true", "-XX:+UnlockDiagnosticVMOptions")
    autoReloadPlugins.set(true)
  }

  patchPluginXml {
    untilBuild.set(provider { null })

    changeNotes.set(provider {
      changelog.renderItem(
        changelog
          .getLatest()
          .withHeader(false)
          .withEmptySections(false),
        org.jetbrains.changelog.Changelog.OutputType.HTML
      )
    })
  }

}
