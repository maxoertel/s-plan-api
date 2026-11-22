plugins {
    id("convention.kotlin.spring.library")
}

dependencies {
    api(project(":user-management-domain"))
    implementation(project(":application-security"))
}
