import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.23"
    id("org.openjfx.javafxplugin") version "0.0.13"
    application
    kotlin("plugin.serialization") version "1.9.23"
}

group = "SAE-42"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
val ktor_version: String by project
val coroutines_version = "1.6.0"

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.controlsfx:controlsfx:11.1.2")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-apache:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:$coroutines_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation("org.openjfx:javafx-media:19")
}

javafx {
    version = "19"
    modules("javafx.controls", "javafx.fxml", "javafx.media")
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<ProcessResources>("processResources") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}


sourceSets {
    main {
        resources {
            srcDirs("src/main/resources")
        }
    }
}


