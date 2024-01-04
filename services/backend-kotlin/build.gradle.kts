plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.8.0" apply (false)
    kotlin("plugin.serialization") version ("1.8.0") apply (false)
    kotlin("plugin.spring") version "1.8.0" apply (false)
    kotlin("plugin.jpa") version "1.8.0" apply (false)

    id("org.springframework.boot") version "3.0.1" apply (false)
    id("io.spring.dependency-management") version "1.1.0" apply (false)
}

allprojects {
    group = "com.tsypk"
    version = "1.0"

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    dependencies {
        implementation(kotlin("stdlib-jdk8"))
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

        implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.springframework.boot:spring-boot-starter-jdbc")
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-test")
        implementation("org.springframework.retry:spring-retry")
        implementation("org.springframework.boot:spring-boot-starter-aop")
        implementation("org.springframework:spring-test")

        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("com.google.code.gson:gson:2.9.0")

        runtimeOnly("org.postgresql:postgresql")

        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

        testImplementation("org.junit.jupiter:junit-jupiter-params:5.1.0")
        testImplementation("org.assertj:assertj-core:3.24.2")
        testImplementation(kotlin("test"))
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
