plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("KotlinLibraryConventionPlugin") {
            id = "convention.kotlin.library"
            implementationClass = "com.sauter_gmbh.convention.plugin.KotlinLibraryConventionPlugin"
        }

        register("KotlinSpringLibraryConventionPlugin") {
            id = "convention.kotlin.spring.library"
            implementationClass = "com.sauter_gmbh.convention.plugin.KotlinSpringLibraryConventionPlugin"
        }

        register("SpringApplicationConventionPlugin") {
            id = "convention.spring.application"
            implementationClass = "com.sauter_gmbh.convention.plugin.SpringApplicationConventionPlugin"
        }
    }
}
