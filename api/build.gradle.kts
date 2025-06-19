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

val integrationTest = sourceSets.create("integration") {
    java.srcDir("$projectDir/src/integration/java")
    resources.srcDir("$projectDir/src/integration/resources")
    val output = sourceSets["main"].output + sourceSets["test"].output
    compileClasspath += output
    runtimeClasspath += output
}

configurations["integrationImplementation"].extendsFrom(configurations["testImplementation"])
configurations["integrationRuntimeOnly"].extendsFrom(configurations["testRuntimeOnly"])

dependencies {
    implementation(project(":core"))
    implementation("com.sparkjava:spark-core:2.7.2")
    implementation("org.json:json:20250107")
    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("org.slf4j:slf4j-simple:2.0.17")

    sourceSets.named("integration") {
        testImplementation("io.rest-assured:rest-assured:5.5.0")
        testImplementation("io.rest-assured:json-schema-validator:5.5.0")
        testImplementation("org.json:json:20250107")
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes("Main-Class" to "${project.group}.$mainClassName")
    }
}

tasks.register<Test>("integrationTest") {
    description = "Runs the integration tests."
    group = "verification"
    useJUnitPlatform()
    testClassesDirs = integrationTest.output.classesDirs
    classpath = integrationTest.runtimeClasspath
    filter {
        includeTestsMatching("org.gbl.BirthdayReminderSuite")
    }
    shouldRunAfter(tasks.test)
}
