plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.3.0"
    id("org.jetbrains.kotlin.jvm") version "1.5.10"
}

intellij {
    type.set("IC")
    // https://www.jetbrains.com/intellij-repository/releases
    version.set("213.5744.223")
    plugins.set(listOf("IntelliLang", "terminal"))
    pluginName.set("PowerShell")
    updateSinceUntilBuild.set(false)
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

    implementation ("org.eclipse.lsp4j:org.eclipse.lsp4j:0.3.0") {
        exclude("com.google.code.gson:gson")
        exclude("com.google.guava:guava")
    }
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("junit:junit:4.11")
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            apiVersion = "1.4"
            languageVersion = "1.4"
            jvmTarget = "1.8"
        }
    }

    prepareSandbox {
        from("${project.rootDir}/language_host/current") {
            into("${intellij.pluginName.get()}/lib/")
        }
    }
}
