plugins {
    id("convention.kotlin.spring.library")
}

dependencies {
    api(project(":location-domain"))
    implementation(project(":application-security"))
    implementation(project(":mission-domain"))
}
