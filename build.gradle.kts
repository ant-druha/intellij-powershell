import org.jetbrains.intellij.tasks.PrepareSandboxTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.security.MessageDigest
import java.util.zip.ZipFile

plugins {
  id("java")
  alias(libs.plugins.changelog)
  alias(libs.plugins.gradleJvmWrapper)
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
version = "2.6.1"

repositories {
  mavenCentral()
  ivy {
    url = uri("https://github.com/PowerShell/PSScriptAnalyzer/releases/download/")
    patternLayout { artifact("[revision]/[module].[revision].[ext]") }
    content { includeGroup("PSScriptAnalyzer") }
    metadataSources { artifact() }
  }
  ivy {
    url = uri("https://github.com/PowerShell/PowerShellEditorServices/releases/download/")
    patternLayout { artifact("v[revision]/[module].[ext]") }
    content { includeGroup("PowerShellEditorServices") }
    metadataSources { artifact() }
  }
}

val psScriptAnalyzerVersion: String by project
val psScriptAnalyzerSha256Hash: String by project
val psScriptAnalyzer: Configuration by configurations.creating

val psesVersion: String by project
val psesSha256Hash: String by project
val powerShellEditorServices: Configuration by configurations.creating

dependencies {
  implementation(libs.bundles.junixsocket)

  implementation(libs.lsp4j)
  implementation(libs.lsp4jdebug)
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
  testImplementation(libs.junit)

  psScriptAnalyzer(
    group = "PSScriptAnalyzer",
    name = "PSScriptAnalyzer",
    version = psScriptAnalyzerVersion,
    ext = "nupkg"
  )

  powerShellEditorServices(
    group = "PowerShellEditorServices",
    name = "PowerShellEditorServices",
    version = psesVersion,
    ext = "zip"
  )
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

    options.apply {
      compilerArgs.add("-Werror")
      encoding = "UTF-8"
    }
    sourceCompatibility = "17"
    targetCompatibility = "17"
  }
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    dependsOn(generateLexer, generateParser)

    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_17)
      allWarningsAsErrors = true
    }
  }

  fun File.verifyHash(expectedHash: String) {
    println("Calculating hash for $name...")
    val data = readBytes()
    val hash = MessageDigest.getInstance("SHA-256").let { sha256 ->
      sha256.update(data)
      sha256.digest().joinToString("") { "%02X".format(it) }
    }
    println("Expected hash for $name = $expectedHash")
    println("Calculated hash for $name = $hash")
    if (!hash.equals(expectedHash, ignoreCase = true)) {
      error("$name hash check failed.\n" +
        "Please try re-downloading the dependency, or update the expected hash in the gradle.properties file.")
    }
  }

  val verifyPsScriptAnalyzer by registering {
    dependsOn(psScriptAnalyzer)
    inputs.property("hash", psScriptAnalyzerSha256Hash)
    doFirst {
      psScriptAnalyzer.singleFile.verifyHash(psScriptAnalyzerSha256Hash)
    }
  }

  fun PrepareSandboxTask.unpackPsScriptAnalyzer(outDir: String) {
    dependsOn(psScriptAnalyzer, verifyPsScriptAnalyzer)

    from(zipTree(psScriptAnalyzer.singleFile)) {
      into("$outDir/PSScriptAnalyzer")

      // NuGet stuff:
      exclude("_manifest/**", "_rels/**", "package/**", "[Content_Types].xml", "*.nuspec")

      // Compatibility profiles, see https://github.com/PowerShell/PSScriptAnalyzer/issues/1148
      exclude("compatibility_profiles/**")
    }
  }

  val verifyPowerShellEditorServices by registering {
    dependsOn(powerShellEditorServices)
    inputs.property("hash", psesSha256Hash)
    doFirst {
      powerShellEditorServices.singleFile.verifyHash(psesSha256Hash)
    }
  }

  fun PrepareSandboxTask.unpackPowerShellEditorServices(outDir: String) {
    dependsOn(powerShellEditorServices, verifyPowerShellEditorServices)

    from(zipTree(powerShellEditorServices.singleFile)) {
      into("$outDir/")
      // We only need this module and not anything else from the archive:
      include("PowerShellEditorServices/**")
    }
  }

  withType<PrepareSandboxTask> {
    val outDir = "${intellij.pluginName.get()}/lib/LanguageHost/modules"
    unpackPsScriptAnalyzer(outDir)
    unpackPowerShellEditorServices(outDir)
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
