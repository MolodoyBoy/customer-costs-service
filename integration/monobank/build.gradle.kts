dependencies {
    implementation(project(":domain"))
    implementation(project(":reload"))

    implementation(libs.springWebFlux)
    implementation(libs.springBootStarter)
    implementation("com.fasterxml.jackson.core:jackson-annotations")
}