plugins {
    id("buildlogic.java-application-conventions")
}

group = "org.gbl"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin:6.5.0")
    implementation("io.javalin:javalin-rendering:6.5.0")
    implementation("gg.jte:jte:2.2.1")
    implementation("org.slf4j:slf4j-simple:2.0.11")
    implementation(project(":core"))
    implementation(project(":cli"))
    implementation("io.vavr:vavr:0.9.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.2")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.17.2")
    implementation("org.hibernate.validator:hibernate-validator:8.0.0.Final")

    testImplementation("io.javalin:javalin-testtools:6.5.0")
}

tasks.test {
    useJUnitPlatform()
}