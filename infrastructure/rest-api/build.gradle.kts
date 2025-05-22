plugins {
    id("org.openapi.generator")
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":analytics-domain"))
    implementation(project(":infrastructure:security"))
    implementation(project(":infrastructure:rest-api:ccs-openapi"))

    implementation(libs.springBootWeb)
    implementation(libs.bundles.swagger)
}

sourceSets {
    main {
        java {
            srcDirs(
                "src/main/java",
                "$buildDir/generated/src/main/java"
            )
        }
    }
}

openApiGenerate {
    inputSpec.set("$projectDir/ccs-openapi/src/main/resources/openapi.yaml")

    generatorName.set("spring")
    library.set("spring-boot")

    outputDir.set("$buildDir/generated")

    apiPackage.set("com.oleg.customer.costs.api")
    modelPackage.set("com.oleg.customer.costs.model")

    modelNameSuffix = "Dto"
    configOptions.set(
        mapOf(
            "interfaceOnly" to "true",
            "useTags"       to "true",
            "dateLibrary"   to "java8",
            "useSpringBoot3" to "true",
            "implicitHeaders" to "true",
            "openApiNullable" to "false",
            "modelNameSuffix"   to "Dto",
            "useBeanValidation" to "false",
            "useResponseEntity" to "false",
            "skipDefaultInterface" to "true",
            "annotationLibrary" to "swagger2",
            "hideGenerationTimestamp" to "true"
        )
    )
}

tasks.compileJava {
    dependsOn("openApiGenerate")
}