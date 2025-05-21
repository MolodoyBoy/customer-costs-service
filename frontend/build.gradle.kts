import com.github.gradle.node.npm.task.NpmTask

plugins {
    id("org.openapi.generator")
    id("com.github.node-gradle.node")
}

node {
    version.set("20.5.0")
    npmVersion.set("10.1.0")
    download.set(true)
    workDir.set(layout.buildDirectory.dir("nodejs"))
    npmWorkDir.set(layout.buildDirectory.dir("npm"))
}

val npmInstall = tasks.named("npmInstall")

val npmBuild = tasks.register<NpmTask>("npmBuild") {
    dependsOn(npmInstall)
    args.set(listOf("run", "build"))
}

artifacts {
    add("archives", file("$projectDir/build"))
}

val openApiSpec = "${rootProject.projectDir}/infrastructure/rest-api/ccs-openapi/src/main/resources/openapi.yaml"

openApiGenerate {
    generatorName.set("javascript")
    inputSpec.set(openApiSpec)
    outputDir.set("$buildDir/generated-js-client")
    apiPackage.set("api")
    modelPackage.set("model")
    additionalProperties.set(
        mapOf(
            "supportsES6" to "true",
            "requestBodyAsJson"  to "true",
            "projectName"  to "my-js-client"
        )
    )
}

tasks.named<com.github.gradle.node.npm.task.NpmInstallTask>("npmInstall").configure {
    dependsOn("openApiGenerate")
}