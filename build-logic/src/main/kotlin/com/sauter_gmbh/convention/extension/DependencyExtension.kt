package com.sauter_gmbh.convention.extension

import org.gradle.api.Project

internal fun Project.moduleImplementation(projectPath: String) {
    with(dependencies) {
        if (path != projectPath) {
            add("implementation", project(projectPath))
        }
    }
}

internal fun Project.moduleApi(projectPath: String) {
    with(dependencies) {
        if (path != projectPath) {
            add("api", project(projectPath))
        }
    }
}