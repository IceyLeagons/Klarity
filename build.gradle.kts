import com.strumenta.antlrkotlin.gradle.AntlrKotlinTask
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

plugins {
    kotlin("multiplatform") version "1.9.22"
    id("org.jetbrains.dokka") version "1.9.10"
    java
    antlr
    id("com.strumenta.antlr-kotlin") version "0.1.0-RC14"
    `maven-publish`
}

val klarityVersion = "2.1.0"

group = "net.iceyleagons"
version = klarityVersion

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
        commonMain {
            dependencies {
                implementation("com.strumenta:antlr-kotlin-runtime:0.1.0-RC14")
            }

            kotlin {
                srcDir(layout.buildDirectory.dir("generatedAntlr"))
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        jsMain {

        }

        val jvmMain by getting {
            dependencies {
                implementation("org.json:json:20230618")
            }
        }
        val jvmTest by getting
        val jsMain by getting {
            dependencies {
                implementation(project.dependencies.platform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:1.0.0-pre.674"))
                implementation("org.jetbrains.kotlin:kotlin-stdlib-js:1.9.0")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-js")
            }
        }
        val jsTest by getting
    }
}

dependencies {
    antlr("org.antlr:antlr4:4.13.1")
}


val generateKotlinGrammarSource = tasks.register<AntlrKotlinTask>("generateKotlinGrammarSource") {
    dependsOn("cleanGenerateKotlinGrammarSource")

    // ANTLR .g4 files are under
    setSource(layout.projectDirectory.dir("src/antlr"))

    // We want the generated source files to have this package name
    val pkgName = "net.iceyleagons.klarity.parsers.generated"
    packageName = pkgName

    // We want visitors alongside listeners.
    // The Kotlin target language is implicit, as is the file encoding (UTF-8)
    arguments = listOf("-visitor")

    // Generated files are outputted inside build/generatedAntlr/{package-name}
    val outDir = "generatedAntlr/${pkgName.replace(".", "/")}"
    outputDirectory = layout.buildDirectory.dir(outDir).get().asFile
}

tasks.dokkaHtml {
    outputDirectory.set(File("docs"))
}

tasks.withType<KotlinCompile<*>> {
    dependsOn(generateKotlinGrammarSource)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "net.iceyleagons"
            artifactId = "klarity"
            version = klarityVersion

            from(components["java"])
        }
    }
}