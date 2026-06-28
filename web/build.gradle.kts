plugins {
    id("buildlogic.java-application-conventions")
}

val mainClassName = "Main"

group = "org.gbl"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    // core deps bundle
    implementation(libs.bundles.client)

    // modules
    implementation(project(":shared"))
    implementation(project(":core"))

    // web server
    implementation("io.javalin:javalin:6.5.0")
    implementation("io.javalin:javalin-rendering:6.5.0")
    implementation("gg.jte:jte:2.2.1")
    implementation("org.slf4j:slf4j-simple:2.0.11")

    // test
    testImplementation("io.javalin:javalin-testtools:6.5.0")
}

application {
    mainClass = "${project.group}.$mainClassName"
}

tasks.test {
    useJUnitPlatform()
}