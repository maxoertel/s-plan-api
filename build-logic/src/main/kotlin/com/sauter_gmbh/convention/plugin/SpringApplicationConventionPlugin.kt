package com.sauter_gmbh.convention.plugin

import com.sauter_gmbh.convention.extension.libs
import com.sauter_gmbh.convention.extension.moduleApi
import com.sauter_gmbh.convention.extension.moduleImplementation
import com.sauter_gmbh.convention.extension.pluginIdOrThrow
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
class SpringApplicationConventionPlugin : Plugin<Project> {

  override fun apply(target: Project) {
    with(target) {

      /* ------------------ APPLY PLUGINS ------------------ */
      with(pluginManager) {
        apply(libs.pluginIdOrThrow("kotlin-jvm"))
        apply(libs.pluginIdOrThrow("kotlin-spring"))
        apply(libs.pluginIdOrThrow("spring-boot"))
        apply(libs.pluginIdOrThrow("spring-dependency-management"))
      }

      /* ------------------ CONFIGURE EXTENSIONS ------------------ */
      // none

      /* ------------------ CONFIGURE DEPENDENCIES ------------------ */
      with(dependencies) {
        add("implementation", "org.springframework.boot:spring-boot-starter-webmvc")
        add("implementation", "org.springframework.boot:spring-boot-starter-jdbc")
        add("implementation", "org.jetbrains.kotlin:kotlin-reflect")
        add("implementation", "tools.jackson.module:jackson-module-kotlin")
        add("implementation", libs.findLibrary("slf4j-api").get())

        add("runtimeOnly", "org.postgresql:postgresql")

        add("testImplementation", "org.springframework.boot:spring-boot-starter-webmvc-test")
        add("testImplementation", "org.jetbrains.kotlin:kotlin-test-junit5")
        add("testImplementation", libs.findLibrary("mockk").get())
        add("testImplementation", libs.findLibrary("springmockk").get())
        add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher")

        moduleApi(":shared-core")
      }
    }
  }
}
