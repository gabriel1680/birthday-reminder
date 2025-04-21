plugins {
    id("buildlogic.java-application-conventions")
}

val mainClassName = "Main"

group = "org.gbl"
version = "1.0.0-alpha"

repositories {
    mavenCentral()
}

application {
    mainModule = "${project.group}"
    mainClass = "${project.group}.$mainClassName"
}

dependencies {
    implementation(project(":core"))
    implementation("com.sparkjava:spark-core:2.7.2")
    implementation("org.json:json:20250107")
    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("org.slf4j:slf4j-simple:2.0.17")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes("Main-Class" to "${project.group}.$mainClassName")
    }
}
