plugins {
    id("java")
    id("application")
}

group = "org.gbl"
version = "unspecified"

repositories {
    mavenCentral()
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