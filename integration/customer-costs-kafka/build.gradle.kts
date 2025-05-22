dependencies {
    implementation(project(":domain"))
    implementation(project(":reload"))

    implementation(project(":analytics-domain"))
    implementation(project(":analytics-reload"))

    implementation(libs.springKafka)
    implementation(libs.springBootStarter)
    implementation("com.fasterxml.jackson.core:jackson-core")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.core:jackson-annotations")
}