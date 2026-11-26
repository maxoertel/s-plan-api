plugins {
    id("convention.kotlin.spring.library")
}

dependencies {
    implementation(project(":infra-db"))
    implementation(project(":delivery-notes-domain"))
}
