plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.spotless)
}

allprojects {
    repositories {
        mavenCentral()
    }

    group = "de.sauter-gmbh"
    version = "0.0.1-SNAPSHOT"
}

subprojects {
    pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
        configure<JavaPluginExtension> {
            toolchain {
                languageVersion = JavaLanguageVersion.of(17)
            }
        }

        configure<org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension> {
            compilerOptions {
                freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
            }
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

spotless {
    // ---------- KOTLIN ----------
    kotlin {
        ktfmt().googleStyle()

        target("**/*.kt")
        targetExclude("**/build/**/*.kt", "build-logic/**/*.kt")
    }

    // ---------- GRADLE KOTLIN DSL ----------
    kotlinGradle {
        ktlint("1.5.0")
        target("**/*.kts")
    }

    // ---------- MARKDOWN / JSON / YAML ----------
    format("misc") {
        target("**/*.md", "**/*.yaml", "**/*.yml", "**/*.json")
        trimTrailingWhitespace()
        endWithNewline()
    }
}
