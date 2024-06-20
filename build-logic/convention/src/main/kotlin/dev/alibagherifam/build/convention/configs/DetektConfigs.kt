package dev.alibagherifam.build.convention.configs

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import java.io.File

internal fun Project.applyDetektPlugin() {
    pluginManager.apply("io.gitlab.arturbosch.detekt")

    extensions.configure<DetektExtension> {
        buildUponDefaultConfig = true
        addConfig(
            project = this@applyDetektPlugin,
            newConfig = DetektConfig.Core
        )

        baseline = getDetektBaselineFile()
    }

    tasks.withType<Detekt>().configureEach {
        jvmTarget = JavaVersion.VERSION_11.toString()
        reports {
            md.required.set(false)
            sarif.required.set(false)
            txt.required.set(false)
            xml.required.set(false)
        }
    }
}

internal fun DetektExtension.addConfig(
    project: Project,
    newConfig: DetektConfig
) {
    config.add(project.getDetektConfigFile(newConfig))
}

internal enum class DetektConfig(val fileName: String) {
    Core("detekt-core.yml"),
    Compose("detekt-compose.yml")
}

private fun ConfigurableFileCollection.add(file: File) {
    setFrom(from + file)
}

private fun Project.getDetektConfigFile(config: DetektConfig): File =
    file("$configDirectoryPath/${config.fileName}")

private fun Project.getDetektBaselineFile(): File =
    file("$configDirectoryPath/baseline/detekt$path.xml")

private val Project.configDirectoryPath: String
    get() = "${project.rootDir}/config"
