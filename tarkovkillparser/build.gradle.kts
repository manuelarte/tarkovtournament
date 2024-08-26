plugins {
    kotlin("jvm")
    id("pl.allegro.tech.build.axion-release")
    id("org.jlleitschuh.gradle.ktlint")
}

group = "org.manuel.games"
version = scmVersion.version

dependencies {
    implementation("org.openpnp:opencv:4.9.0-0")
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
