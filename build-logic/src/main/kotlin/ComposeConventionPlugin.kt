import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import common.implementation
import common.getVersionCatalogs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType

@Suppress("unused")
class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = getVersionCatalogs()

            val androidExtension = extensions.let {
                it.findByType<LibraryExtension>() ?: it.findByType<ApplicationExtension>()
            } ?: error("Plugin should be applied on either Android Application or Library")

            enableComposeCompiler(androidExtension, libs)
            addComposeDependencies(libs)
        }
    }

    private fun enableComposeCompiler(
        android: CommonExtension<*, *, *, *>,
        libs: VersionCatalog
    ) {
        android.apply {
            buildFeatures.compose = true

            composeOptions.kotlinCompilerExtensionVersion =
                libs.findVersion("androidxComposeCompiler").get().toString()
        }
    }

    private fun Project.addComposeDependencies(libs: VersionCatalog) {
        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))
            implementation(libs, "androidx-compose-material3")
            implementation(libs, "androidx-compose-tooling-preview")
            implementation(libs, "androidx-compose-tooling")
            add(
                "detektPlugins",
                libs.findLibrary("detekt-composeRules").get()
            )
        }
    }
}
