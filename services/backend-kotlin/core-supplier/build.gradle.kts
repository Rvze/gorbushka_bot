plugins {
    kotlin("kapt")
}

dependencies {
    implementation(project(":library-recognition"))

    implementation("com.google.guava:guava:31.1-jre")
    //mapstruct
    implementation("org.mapstruct:mapstruct:1.5.3.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.3.Final")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
