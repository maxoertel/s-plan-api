plugins {
    id("convention.kotlin.spring.library")
}

dependencies {
    implementation(project(":infra-db"))
    implementation(project(":application-security"))
    implementation(project(":domain-identity"))

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-web")
}
