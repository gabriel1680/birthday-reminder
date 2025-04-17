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
    implementation("io.vavr:vavr:0.9.0")
    implementation("com.google.code.gson:gson:2.12.1")
}

application {
    mainClass = "org.gbl.Main"
}

tasks.test {
    useJUnitPlatform()
}

val fatJar = tasks.register<Jar>("fatJar") {
    dependsOn.addAll(listOf("compileJava", "processResources"))
    archiveClassifier.set("standalone")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest { attributes(mapOf("Main-Class" to application.mainClass)) }
    val sourcesMain = sourceSets.main.get()
    val contents = configurations.runtimeClasspath.get()
        .map { if (it.isDirectory) it else zipTree(it) } +
            sourcesMain.output
    from(contents)
}

tasks.build {
    dependsOn(fatJar)
}