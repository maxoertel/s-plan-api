package com.sauter_gmbh.convention.plugin

import com.sauter_gmbh.convention.extension.libs
import com.sauter_gmbh.convention.extension.moduleApi
import com.sauter_gmbh.convention.extension.moduleImplementation
import com.sauter_gmbh.convention.extension.pluginIdOrThrow
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
class KotlinLibraryConventionPlugin : Plugin<Project> {

  override fun apply(target: Project) {
    with(target) {

      /* ------------------ APPLY PLUGINS ------------------ */
      with(pluginManager) {
        apply(libs.pluginIdOrThrow("kotlin-jvm"))
      }

      /* ------------------ CONFIGURE EXTENSIONS ------------------ */
      // none

      /* ------------------ CONFIGURE DEPENDENCIES ------------------ */
      with(dependencies) {
        add("testImplementation", "org.jetbrains.kotlin:kotlin-test-junit5")
        add("testImplementation", libs.findLibrary("mockk").get())
        add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher")
        add("implementation", libs.findLibrary("slf4j-api").get())

        moduleApi(":shared-core")
      }
    }
  }
}
