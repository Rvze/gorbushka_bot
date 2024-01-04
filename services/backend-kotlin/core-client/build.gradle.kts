plugins {}

dependencies {
    implementation(project(":library-recognition"))

    implementation("com.google.guava:guava:31.1-jre")

    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j:3.0.0")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.0.0")
    implementation("org.springframework.cloud:spring-cloud-openfeign-core:4.0.0")

    implementation("dev.inmo:tgbotapi:4.1.3")
    implementation("dev.inmo:tgbotapi.core:4.1.3")
    implementation("dev.inmo:tgbotapi-jvm:4.1.3")
    implementation("dev.inmo:tgbotapi.api:4.1.3")
    implementation("dev.inmo:tgbotapi.core-jvm:4.1.3")
    implementation("dev.inmo:tgbotapi.api-jvm:4.1.3")
}

tasks.register("prepareKotlinBuildScriptModel") {}
