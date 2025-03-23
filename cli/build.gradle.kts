plugins {
    id("buildlogic.java-application-conventions")
}

group = "org.gbl"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation("info.picocli:picocli:4.7.0")
}

tasks.test {
    useJUnitPlatform()
}