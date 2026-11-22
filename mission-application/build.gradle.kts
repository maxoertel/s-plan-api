plugins {
    id("convention.kotlin.spring.library")
}

dependencies {
    api(project(":mission-domain"))
    implementation(project(":application-security"))
}
