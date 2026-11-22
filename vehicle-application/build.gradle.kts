plugins {
    id("convention.kotlin.spring.library")
}

dependencies {
    api(project(":vehicle-domain"))
    implementation(project(":application-security"))
}
