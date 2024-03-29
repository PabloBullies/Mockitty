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
    implementation("net.bytebuddy:byte-buddy-agent:1.14.12")
    implementation("org.objenesis:objenesis:3.3")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
    testImplementation ("ch.qos.logback:logback-classic:1.3.5")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}