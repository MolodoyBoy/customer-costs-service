plugins {
    id("org.springframework.boot")
    id("com.google.cloud.tools.jib")
}

tasks.jar {
    enabled = false
}

springBoot {
    buildInfo()
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":reload"))
    implementation(project(":analytics-domain"))
    implementation(project(":analytics-reload"))
    implementation(project(":integration:chatGPT"))
    implementation(project(":integration:monobank"))
    implementation(project(":infrastructure:security"))
    implementation(project(":infrastructure:rest-api"))
    implementation(project(":integration:customer-costs-kafka"))
    implementation(project(":infrastructure:rest-api:ccs-openapi"))
    implementation(project(":integration:customer-costs-database"))
    implementation(project(":integration:customer-costs-analytics-database"))

    implementation(libs.chatGPT)
    implementation(libs.springKafka)
    implementation(libs.bundles.jooq)
    implementation(libs.springOauth2)
    implementation(libs.springBootWeb)
    implementation(libs.springBootJdbc)
    implementation(libs.bundles.swagger)
    implementation(libs.springBootStarter)
}

val dockerHubUsername: String by project
val dockerHubPassword = System.getenv("DOCKER_HUB_PASSWORD")
val imageVersion = System.getenv().getOrDefault("IMAGE_VERSION", "")

jib {
    from {
        image = "eclipse-temurin:21-jdk"
    }

    to {
        image = "molodoyboy777/customer-costs-service:${imageVersion}"

        auth {
            username = dockerHubUsername
            password = dockerHubPassword
        }
    }

    container {
        jvmFlags = listOf("-XX:MaxRAMPercentage=80")
        mainClass = "com.oleg.customer.costs.CustomerCostsServiceApplication"
    }
}