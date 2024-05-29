plugins {
    kotlin("jvm") version "2.0.0-RC3"
    kotlin("plugin.serialization") version "2.0.0-RC3"
    application
}

group = "com.starsrealm.jbmodelconvert"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.0-RC")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("MainKt")
}

tasks.register<Jar>("fatJar") {
    archiveClassifier.set("all")
    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    })

    manifest {
        attributes["Main-Class"] = "com.starsrealm.jbmodelconvert.MainKt"
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
