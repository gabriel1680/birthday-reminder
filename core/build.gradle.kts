plugins {
    id("buildlogic.java-library-conventions")
}

group = "org.gbl"

repositories {
    mavenCentral()
}

dependencies {
}

tasks.test {
    useJUnitPlatform()
}