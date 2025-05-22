dependencies {
    implementation(project(":domain"))
    implementation(project(":analytics-domain"))

    implementation(libs.springOauth2)
    implementation(libs.springBootWeb)
    implementation(libs.bundles.swagger)
}