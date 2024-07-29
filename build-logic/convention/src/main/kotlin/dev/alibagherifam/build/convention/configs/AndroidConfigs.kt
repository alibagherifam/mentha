package dev.alibagherifam.build.convention.configs

import com.android.build.api.dsl.CommonExtension
import dev.alibagherifam.build.convention.libsCatalog
import org.gradle.api.Project

internal fun CommonExtension<*, *, *, *, *, *>.configureAndroidBaseOptions(
    project: Project
) {
    with(project) {
        configureAndroidCompilation(project, libs = libsCatalog)

        applyDetektPlugin()
        excludeRedundantResourcesFromApk()
        specifySupportedLanguages()
        configureLint()

        defaultConfig.testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }
}

private fun CommonExtension<*, *, *, *, *, *>.excludeRedundantResourcesFromApk() {
    packaging.resources.excludes += setOf(
        "/META-INF/{AL2.0,LGPL2.1}",
        "/META-INF/LICENSE.md",
        "/META-INF/LICENSE-notice.md"
    )
}

private fun CommonExtension<*, *, *, *, *, *>.specifySupportedLanguages() {
    defaultConfig {
        resourceConfigurations += setOf("en", "fa")
    }
}

private fun CommonExtension<*, *, *, *, *, *>.configureLint() {
    lint {
        checkDependencies = true
        ignoreTestSources = true
    }
}
