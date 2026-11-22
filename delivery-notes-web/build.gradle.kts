plugins {
    id("convention.kotlin.spring.library")
}

dependencies {
    implementation(project(":infra-web"))
    implementation(project(":delivery-notes-application"))
}
