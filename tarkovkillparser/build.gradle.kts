plugins {
    kotlin("jvm")
    id("pl.allegro.tech.build.axion-release")
    id("org.jlleitschuh.gradle.ktlint")
}

group = "org.manuel.games"
version = scmVersion.version

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.openpnp:opencv:4.9.0-0")
    implementation("com.github.pemistahl:lingua:1.2.2")
    implementation("net.sourceforge.tess4j:tess4j:5.13.0")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    implementation("ch.qos.logback:logback-classic:1.5.7")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
