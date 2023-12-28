plugins {
    kotlin("multiplatform") version "1.9.0"
    id("org.jetbrains.dokka") version "1.8.20"
    java
    `maven-publish`
}

group = "net.iceyleagons"
version = "2.0.0"

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
            version = "2.0.0"

            from(components["java"])
        }
    }
}