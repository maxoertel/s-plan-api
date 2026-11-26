plugins {
    id("convention.kotlin.spring.library")
}

dependencies {
    api("org.springframework:spring-webmvc")
    api("com.fasterxml.jackson.core:jackson-annotations")
    compileOnly("org.springframework.security:spring-security-core")
    testImplementation("org.springframework.security:spring-security-core")
}
