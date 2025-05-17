pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    val springBootVersion: String by settings

    plugins {
        id("java")
        id("nu.studer.jooq") version ("9.0")
        id("org.flywaydb.flyway") version ("10.14.0")
        id("org.openapi.generator") version ("7.13.0")
        id("com.google.cloud.tools.jib") version ("3.4.1")
        id("io.spring.dependency-management") version ("1.1.7")
        id("org.springframework.boot") version (springBootVersion)
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            library("postrgesql", "org.postgresql", "postgresql").withoutVersion()
            library("flywayCore", "org.flywaydb:flyway-core:10.14.0")
            library("flywayPostgres", "org.flywaydb:flyway-database-postgresql:10.14.0")

            library("springContext", "org.springframework", "spring-context").withoutVersion()
            library("springAutoConfigure", "org.springframework", "spring-boot-autoconfigure").withoutVersion()
            library("springWeb", "org.springframework", "spring-web").withoutVersion()
            library("springBootWeb", "org.springframework.boot", "spring-boot-starter-web").withoutVersion()
            library("springBootActuator", "org.springframework.boot", "spring-boot-starter-actuator").withoutVersion()
            library("springBootJdbc", "org.springframework.boot", "spring-boot-starter-jdbc").withoutVersion()
            library("springSecurity", "org.springframework.boot", "spring-boot-starter-security").withoutVersion()
            library("springBootStarter", "org.springframework.boot", "spring-boot-starter").withoutVersion()
            library("springKafka", "org.springframework.kafka", "spring-kafka").withoutVersion()
            library("javax", "javax.annotation:javax.annotation-api:1.3.2")
            library("springOauth2", "org.springframework.boot", "spring-boot-starter-oauth2-resource-server").withoutVersion()

            library("jooqCodegen", "org.jooq:jooq-codegen:3.19.9")
            library("jooq.core", "org.jooq:jooq:3.19.9")
            library("jooq.extensions", "org.jooq:jooq-postgres-extensions:3.19.9")
            library("jakarta.xml", "jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
            bundle(
                "jooq", listOf(
                    "jooq.core",
                    "jooq.extensions",
                    "jakarta.xml"
                )
            )

            library("springdoc", "org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
            library("swagger.annotations", "io.swagger:swagger-annotations:1.6.11")
            bundle(
                "swagger", listOf(
                    "springdoc",
                    "swagger.annotations"
                )
            )

            library("spring.retry", "org.springframework.retry", "spring-retry").withoutVersion()
            library("spring.aspects", "org.springframework", "spring-aspects").withoutVersion()
            bundle(
                "retry", listOf(
                    "spring.retry",
                    "spring.aspects"
                )
            )
        }

        create("testLibs") {
            library("junit.api", "org.junit.jupiter", "junit-jupiter-api").withoutVersion()
            library("junit.engine", "org.junit.jupiter", "junit-jupiter-engine").withoutVersion()
            library("junit.params", "org.junit.jupiter", "junit-jupiter-params").withoutVersion()
            library("mockito.junit", "org.mockito", "mockito-junit-jupiter").withoutVersion()
            library("mockito.core", "org.mockito", "mockito-core").withoutVersion()
            bundle(
                "common", listOf(
                    "junit.api",
                    "junit.engine",
                    "junit.params",
                    "mockito.junit",
                    "mockito.core"
                )
            )

            library("testContainersJupiter", "org.testcontainers", "junit-jupiter").withoutVersion()
            library("postgresqlContainer", "org.testcontainers", "postgresql").withoutVersion()
            library("kafka", "org.testcontainers", "kafka").withoutVersion()
            library("core", "org.testcontainers", "testcontainers").withoutVersion()
            bundle(
                "testContainers", listOf(
                    "testContainersJupiter",
                    "postgresqlContainer",
                    "kafka",
                    "core"
                )
            )

            library("springBootTest", "org.springframework.boot", "spring-boot-starter-test").withoutVersion()
        }
    }
}

rootProject.name = "customer-costs-service"

include("domain")
include("integration")
include("infrastructure")
include("infrastructure:security")
include("infrastructure:rest-api")
include("infrastructure:application")
include("integration:customer-costs-reload")
include("integration:customer-costs-kafka")
include("integration:customer-costs-database")
include("infrastructure:rest-api:ccs-openapi")
include("infrastructure:webpack")