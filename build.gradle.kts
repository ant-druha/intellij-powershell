import org.jetbrains.intellij.tasks.PrepareSandboxTask

plugins {
  id("java")
  id("org.jetbrains.changelog") version "2.2.0"
  id("org.jetbrains.grammarkit") version "2022.3.2.2"
  id("org.jetbrains.intellij") version "1.15.0"
  id("org.jetbrains.kotlin.jvm") version "1.9.0"
}

intellij {
  type.set("IC")
  version.set("2023.2")
  plugins.set(listOf("org.intellij.intelliLang", "terminal"))
  pluginName.set("PowerShell")
}

sourceSets {
  main {
    java.srcDir("src/main/gen")
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
  implementation("com.kohlschutter.junixsocket:junixsocket-common:2.3.3")
  implementation("com.kohlschutter.junixsocket:junixsocket-native-common:2.3.3")

  implementation("org.eclipse.lsp4j:org.eclipse.lsp4j:0.3.0")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
  testImplementation("junit:junit:4.11")
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
  val genRoot = file("src/main/gen")
  val genPackageDirectory = genRoot.resolve("com/intellij/plugin/powershell/lang")
  generateLexer {
    sourceFile = resources.resolve("_PowerShellLexer.flex")
    targetOutputDir = genPackageDirectory
    defaultCharacterEncoding = "UTF-8"
  }

  generateParser {
    sourceFile = resources.resolve("PowerShell.bnf")
    targetRootOutputDir = genRoot
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
