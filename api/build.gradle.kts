plugins {
    id("java")
    id("application")
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

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.26.3")
    testImplementation("org.mockito:mockito-core:5.14.1")
    testImplementation("org.mockito:mockito-junit-jupiter:5.13.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes("Main-Class" to "${project.group}.$mainClassName")
    }
}

// Create a custom task to copy the core JAR to the dist folder
val copyCoreJarToDist by tasks.registering(Copy::class) {
    dependsOn(":core:build")  // Ensure the core project is built first
    from("$rootDir/core/build/libs/core-1.0.jar")  // Path to the core JAR
    into("$rootDir/dist")  // Copy core JAR to dist folder
}

// Make sure the copyCoreJarToDist task runs after the build task
tasks.build {
    dependsOn(copyCoreJarToDist)
}

// Ensure the API JAR is also copied to the dist folder
val copyApiJarToDist by tasks.registering(Copy::class) {
    dependsOn(tasks.jar)  // Ensure the API JAR is built first
    from(tasks.jar.get().archiveFile)
    into("$rootDir/dist")
}

// Make sure copyApiJarToDist runs after build
tasks.build {
    dependsOn(copyApiJarToDist)
}
