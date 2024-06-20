import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import dev.alibagherifam.build.convention.configs.configureAndroidBaseOptions
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.android")
                apply("com.android.library")
            }

            extensions.configure<LibraryExtension> {
                configureAndroidBaseOptions(target)
            }

            extensions.configure<LibraryAndroidComponentsExtension> {
                disableDebugBuild(this)
            }
        }
    }

    private fun disableDebugBuild(android: LibraryAndroidComponentsExtension) {
        android.apply {
            beforeVariants(selector().withBuildType("debug")) {
                it.enable = false
            }
        }
    }
}
