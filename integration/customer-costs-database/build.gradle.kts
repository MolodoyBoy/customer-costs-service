import org.flywaydb.core.Flyway
import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.*
import org.jooq.meta.jaxb.Target
import org.testcontainers.containers.PostgreSQLContainer

plugins {
    id("nu.studer.jooq")
    id("org.flywaydb.flyway")
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        val springBootVersion: String by project
        classpath((platform("org.springframework.boot:spring-boot-dependencies:${springBootVersion}")))

        classpath(libs.postrgesql)
        classpath(testLibs.bundles.testContainers)
        classpath(libs.flywayCore)
        classpath(libs.bundles.jooq)
        classpath(libs.jooqCodegen)
        classpath(libs.flywayPostgres)
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":reload"))
    implementation(project(":infrastructure:security"))

    implementation(libs.flywayCore)
    implementation(libs.postrgesql)
    implementation(libs.bundles.jooq)
    implementation(libs.flywayPostgres)
    implementation(libs.springBootJdbc)
}

tasks.register("startPostgresContainer") {
    doLast {
        val containerInstance = PostgreSQLContainer<Nothing>("postgres:11.6").apply {
            withDatabaseName("jooq-gen-db")
            start()
        }
        project.ext.set("containerInstance", containerInstance)
    }
}

tasks.register("flywayMigrateJooqGenDb").configure {
    dependsOn(tasks.named("startPostgresContainer"))
    doFirst {
        val containerInstance = project.ext.get("containerInstance") as PostgreSQLContainer<*>
        val migrationsDir = File(project.projectDir, "src/main/resources/db/migration").absolutePath
        val flyway = Flyway.configure()
            .dataSource(
                containerInstance.jdbcUrl,
                containerInstance.username,
                containerInstance.password
            )
            .locations("filesystem:$migrationsDir")
            .load()
        flyway.migrate()
    }
}

tasks.register("jooqCodegen") {
    dependsOn(tasks.named("flywayMigrateJooqGenDb"))
    doLast {
        val containerInstance = project.ext.get("containerInstance") as PostgreSQLContainer<*>

        val configuration = Configuration()
            .withJdbc(
                Jdbc()
                    .withDriver("org.postgresql.Driver")
                    .withUrl(containerInstance.jdbcUrl)
                    .withUser(containerInstance.username)
                    .withPassword(containerInstance.password)
            )
            .withGenerator(
                Generator()
                    .withDatabase(Database()
                        .withInputSchema("public")
                        .withExcludes("flyway_schema_history")
                    )
                    .withTarget(
                        Target()
                            .withPackageName("com.oleg.fund.customer.costs.analytics")
                            .withDirectory(project.layout.projectDirectory.dir("src/main/generated-java").asFile.absolutePath)
                    ).withGenerate(Generate()
                        .withPojos(false)
                        .withDaos(false)
                        .withRecords(false)
                        .withIndexes(false)
                        .withKeys(false)
                        .withSequences(false)
                        .withEmptySchemas(false)
                        .withSerializableInterfaces(false)
                    )
            )
        GenerationTool.generate(configuration)

        containerInstance.stop()
    }
}

sourceSets {
    main {
        java {
            srcDirs(
                "src/main/java",
                "src/main/generated-java"
            )
        }
    }
}