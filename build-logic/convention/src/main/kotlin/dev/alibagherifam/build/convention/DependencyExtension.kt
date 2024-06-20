package dev.alibagherifam.build.convention

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

internal val Project.libsCatalog: VersionCatalog
    get() = extensions
        .getByType<VersionCatalogsExtension>()
        .named("libs")

internal fun VersionCatalog.getRequiredVersion(alias: String): String {
    val version = findVersion(alias)
    if (version.isPresent) {
        return version.get().requiredVersion
    } else {
        error("Could not find a version for `$alias`")
    }
}

internal fun VersionCatalog.getLibrary(alias: String): MinimalExternalModuleDependency {
    val library = findLibrary(alias)
    if (library.isPresent) {
        return library.get().get()
    } else {
        error("Could not find a library for `$alias`")
    }
}
