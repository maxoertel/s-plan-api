plugins {
    id("convention.kotlin.spring.library")
}

dependencies {
    api(project(":delivery-notes-domain"))
    implementation(project(":application-security"))
}
