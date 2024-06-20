import dev.alibagherifam.build.convention.androidCommonExtension
import dev.alibagherifam.build.convention.configs.DetektConfig.Compose
import dev.alibagherifam.build.convention.configs.addConfig
import dev.alibagherifam.build.convention.getLibrary
import dev.alibagherifam.build.convention.libsCatalog
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins.apply("org.jetbrains.kotlin.plugin.compose")

            androidCommonExtension.buildFeatures {
                compose = true
            }

            dependencies {
                addComposeDependencies(libs = libsCatalog)
            }

            enableDetektForCompose()
        }
    }

    private fun DependencyHandlerScope.addComposeDependencies(libs: VersionCatalog) {
        val bom = libs.getLibrary("androidx-compose-bom")
        add("implementation", platform(bom))
        add("androidTestImplementation", platform(bom))
        add("implementation", libs.getLibrary("androidx-compose-material3"))
        add("implementation", libs.getLibrary("androidx-compose-ui-tooling-preview"))
        add("debugImplementation", libs.getLibrary("androidx-compose-ui-tooling"))
        add("detektPlugins", libs.getLibrary("detekt-composeRules"))
    }

    private fun Project.enableDetektForCompose() {
        extensions.configure<DetektExtension> {
            addConfig(
                project = this@enableDetektForCompose,
                newConfig = Compose
            )
        }
    }
}
