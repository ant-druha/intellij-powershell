import org.jetbrains.intellij.tasks.PrepareSandboxTask

plugins {
  id("java")
  alias(libs.plugins.changelog)
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
version = "2.4.0"

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

  withType<PrepareSandboxTask> {
    from("${project.rootDir}/language_host/current") {
      into("${intellij.pluginName.get()}/lib/")
    }
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
