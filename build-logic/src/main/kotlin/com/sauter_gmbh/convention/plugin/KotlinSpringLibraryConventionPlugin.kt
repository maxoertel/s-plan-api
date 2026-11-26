package com.sauter_gmbh.convention.plugin

import com.sauter_gmbh.convention.extension.findVersionOrThrow
import com.sauter_gmbh.convention.extension.libs
import com.sauter_gmbh.convention.extension.moduleApi
import com.sauter_gmbh.convention.extension.pluginIdOrThrow
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
class KotlinSpringLibraryConventionPlugin : Plugin<Project> {

  override fun apply(target: Project) {
    with(target) {

      /* ------------------ APPLY PLUGINS ------------------ */
      with(pluginManager) {
        apply(libs.pluginIdOrThrow("kotlin-jvm"))
        apply(libs.pluginIdOrThrow("kotlin-spring"))
        apply(libs.pluginIdOrThrow("spring-dependency-management"))
      }

      /* ------------------ CONFIGURE EXTENSIONS ------------------ */
      // none

      /* ------------------ CONFIGURE DEPENDENCIES ------------------ */
      val springBootVersion = libs.findVersionOrThrow("spring-boot")

      with(dependencies) {
        add("implementation", platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
        add("implementation", "org.jetbrains.kotlin:kotlin-reflect")
        add("implementation", libs.findLibrary("slf4j-api").get())
        add("implementation", "org.springframework:spring-context")

        add("testImplementation", "org.jetbrains.kotlin:kotlin-test-junit5")
        add("testImplementation", libs.findLibrary("mockk").get())
        add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher")

        moduleApi(":shared-core")
      }
    }
  }
}
