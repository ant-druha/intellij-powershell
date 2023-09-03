plugins {
  id("java")
  id("org.jetbrains.changelog") version "2.2.0"
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
version = "2.0.10"

repositories {
  mavenCentral()
  maven {
    setUrl("https://mvnrepository.com/artifact/com.kohlschutter.junixsocket/junixsocket-common")
  }
}

dependencies {
  implementation("com.kohlschutter.junixsocket:junixsocket-common:2.3.3")
  implementation("com.kohlschutter.junixsocket:junixsocket-native-common:2.3.3")

  implementation("org.eclipse.lsp4j:org.eclipse.lsp4j:0.3.0") {
    exclude("com.google.code.gson:gson")
    exclude("com.google.guava:guava")
  }
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
  testImplementation("junit:junit:4.11")
}

tasks {
  wrapper {
    gradleVersion = "8.3"
  }

  withType<JavaCompile> {
    options.encoding = "UTF-8"
  }
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
  }

  prepareSandbox {
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
