@file:Suppress("UnstableApiUsage")

package com.sauter_gmbh.convention.extension

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugin.use.PluginDependency

internal val Project.libs: VersionCatalog
  get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

@Suppress("NewApi")
fun VersionCatalog.findLibraryOrThrow(name: String): Provider<MinimalExternalModuleDependency> =
  findLibrary(name).orElseThrow {
    kotlin.NoSuchElementException("Library $name not found in version catalog")
  }

fun VersionCatalog.libraryOrThrow(name: String) = findLibraryOrThrow(name).get()

@Suppress("NewApi")
fun VersionCatalog.findPluginOrThrow(name: String): Provider<PluginDependency> =
  findPlugin(name).orElseThrow {
    kotlin.NoSuchElementException("Plugin $name not found in version catalog")
  }

fun VersionCatalog.pluginIdOrThrow(name: String): String = findPluginOrThrow(name).get().pluginId

@Suppress("NewApi")
fun VersionCatalog.findVersionOrThrow(name: String): String =
  findVersion(name)
    .orElseThrow { kotlin.NoSuchElementException("Version $name not found in version catalog") }
    .requiredVersion

fun VersionCatalog.versionIntOrThrow(name: String): Int = findVersionOrThrow(name).toInt()
