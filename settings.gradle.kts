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
include("hw13-creationalPatterns")
include("hw14-behavioralPatterns")
include("hw15-structuralPatterns:demo")
include("hw15-structuralPatterns:homework")
include("hw16-io:demo")
include("hw16-io:homework")
include("hw17-nio")
include("hw18-jdbc:demo")
include("hw18-jdbc:homework")
include("hw19-rdbms")
include("hw20-hibernate")
include("hw21-jpql:class-demo")
include("hw21-jpql:homework")
include("hw22-cache")
include("hw23-noSQL:mongo-db-demo")
include("hw23-noSQL:mongo-db-reactive-demo")
include("hw23-noSQL:neo4j-demo")
include("hw23-noSQL:redis-demo")
include("hw23-noSQL:cassandra-demo")
include("hw23-redis:counter")
include("hw23-redis:data-source")
include("hw23-redis:data-transformer")
include("hw23-redis:data-listener")
include("hw24-webServer")
include ("hw25-di:class-demo")
include ("hw25-di:homework")
include ("hw26-springBootMvc")
include ("hw27-websocket:websocket")
include ("hw27-websocket:messager")
include ("hw27-websocket:messager-starter")
include ("hw27-websocket:application")
include ("hw28-springDataJdbc:class-demo")
include("hw28-springDataJdbc:homework")
include ("hw29-threads")
include ("hw30-JMM")
include ("hw31-executors")
include ("hw32-concurrentCollections:ConcurrentCollections")
include ("hw32-concurrentCollections:QueueHomework")

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
//findProject(":hw28-springDataJdbc:homework")?.name = "homework"
