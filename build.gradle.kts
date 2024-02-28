plugins {
    kotlin("jvm") version "1.9.0"
    `java-library`
}

group = "ru.nsu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("net.bytebuddy:byte-buddy:1.14.12")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}