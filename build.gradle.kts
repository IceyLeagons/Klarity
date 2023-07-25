import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.7.21"
    id("org.jetbrains.dokka") version "1.8.20"
    `maven-publish`
}

group = "net.iceyleagons"
version = "1.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.json:json:20230618")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.dokkaHtml {
    outputDirectory.set(File("docs"))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "net.iceyleagons"
            artifactId = "klarity"
            version = "1.1.0"

            from(components["java"])
        }
    }
}