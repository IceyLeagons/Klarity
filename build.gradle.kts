import org.jetbrains.kotlin.ir.backend.js.compileIr

plugins {
    kotlin("multiplatform") version "1.9.0"
    id("org.jetbrains.dokka") version "1.8.20"
    java
    `maven-publish`
}

group = "net.iceyleagons"
version = "2.0.1"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js {
        compilations["main"].packageJson {
            name = "@iceyleagons/klarity"
            private = false
            customField("repository", mapOf(
                "type" to "git",
                "url" to "https://github.com/IceyLeagons/Klarity"
            ))
            customField("author", "IceyLeagons")
            customField("license", "MIT")
            customField("keywords", arrayOf("i18n", "klarity", "internalization", "library", "kotlin multiplatform", "multiplatform"))
            customField("description", "Klarity is a lightweight internalization library for the Kotlin programming language, with support for JavaScript thanks to Kotlin Multiplatform. It makes it super easy to create multi-lingual projects.")
        }

        nodejs()
        generateTypeScriptDefinitions()
        binaries.library()
    }
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("org.json:json:20230618")
            }
        }
        val jvmTest by getting
        val jsMain by getting {
            dependencies {
                implementation(enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:1.0.0-pre.430"))
                implementation("org.jetbrains.kotlin:kotlin-stdlib-js:1.9.0")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-js")
            }
        }
        val jsTest by getting
    }
}

tasks.dokkaHtml {
    outputDirectory.set(File("docs"))
}
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "net.iceyleagons"
            artifactId = "klarity"
            version = "2.0.1"

            from(components["java"])
        }
    }
}