rootProject.name = "2024-04-otus-java-Kretsev"
include("hw01-gradle")
include("hw02-gradle2")
include("hw02-gradle2-libApi")
include("hw02-gradle2-libApiUse")
include("hw02-logging")
include("hw03-qa")
include("hw04-generics")
include("hw05-collections")
include("hw06-annotations")
include("hw08-gc:demo")
include("hw08-gc:homework")
include("hw09-docker")
include("hw10-byteCodes")
include("hw11-Java8")
include("hw12-solid")

pluginManagement {
    val jgitver: String by settings
    val dependencyManagement: String by settings
    val springframeworkBoot: String by settings
    val johnrengelmanShadow: String by settings
    val jib: String by settings
    val protobufVer: String by settings
    val sonarlint: String by settings
    val spotless: String by settings

    plugins {
        id("fr.brouillard.oss.gradle.jgitver") version jgitver
        id("io.spring.dependency-management") version dependencyManagement
        id("org.springframework.boot") version springframeworkBoot
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
        id("com.google.cloud.tools.jib") version jib
        id("com.google.protobuf") version protobufVer
        id("name.remal.sonarlint") version sonarlint
        id("com.diffplug.spotless") version spotless
    }
}