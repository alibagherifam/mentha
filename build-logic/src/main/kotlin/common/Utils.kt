package common

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.getByType

fun Project.getVersionCatalogs(): VersionCatalog = extensions
    .getByType<VersionCatalogsExtension>()
    .named("libs")

fun DependencyHandlerScope.implementation(
    libs: VersionCatalog,
    dependencyAlias: String
) {
    add(
        "implementation",
        libs.findLibrary(dependencyAlias).get()
    )
}

fun PluginManager.apply(libs: VersionCatalog, pluginAlias: String) {
    apply(libs.findPlugin(pluginAlias).get().get().pluginId)
}

fun VersionCatalog.getRequiredVersion(alias: String): String =
    findVersion(alias).get().requiredVersion
