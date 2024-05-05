import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.23"

    `maven-publish`
    id("io.papermc.paperweight.userdev") version "1.7.0"
}

group = "me.invalidjoker"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("1.20.6-R0.1-SNAPSHOT")

    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.7.1")
    implementation("net.kyori:adventure-text-serializer-plain:4.16.0")
    implementation("net.kyori:adventure-text-minimessage:4.16.0")
}


tasks {
    assemble {
        dependsOn(reobfJar)
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
}

kotlin {
    jvmToolchain(21)
}

// publish to maven local
publishing {
    publications {
        create<MavenPublication>(project.name) {
            from(components["java"])
            artifact(tasks.jar.get().outputs.files.single())

            this.groupId = project.group.toString()
            this.artifactId = project.name.lowercase()
            this.version = project.version.toString()
        }
    }
    repositories {
        mavenLocal()
    }
}