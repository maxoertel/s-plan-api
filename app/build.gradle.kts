description = "s-plan-api"

plugins {
    id("convention.spring.application")
}

dependencies {
    implementation(project(":infra-db"))
    implementation(project(":infra-web"))
    implementation(project(":infra-security"))

    implementation(project(":delivery-notes-web"))
    implementation(project(":location-web"))
    implementation(project(":mission-web"))
    implementation(project(":user-management-web"))
    implementation(project(":vehicle-web"))

    implementation(project(":delivery-notes-infrastructure"))
    implementation(project(":location-infrastructure"))
    implementation(project(":mission-infrastructure"))
    implementation(project(":user-management-infrastructure"))
    implementation(project(":vehicle-infrastructure"))

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-opentelemetry")
    implementation("io.micrometer:micrometer-registry-otlp")
    implementation("com.github.loki4j:loki-logback-appender:1.6.0")

    testImplementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
}
