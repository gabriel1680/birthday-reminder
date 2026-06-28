plugins {
    id("buildlogic.java-library-conventions")
}

group = "org.gbl"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))
    implementation(libs.bundles.client)
}

tasks.test {
    useJUnitPlatform()
}