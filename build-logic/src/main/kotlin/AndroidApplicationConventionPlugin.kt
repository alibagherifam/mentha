import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.android")
                apply("com.android.application")
            }

            extensions.configure<ApplicationExtension> {
                configureAndroidBaseOptions(this)
                configureBuildTypes(this)
                configureLint(this)
                defaultConfig.targetSdk = 33
            }
        }
    }

    private fun configureBuildTypes(android: ApplicationExtension) {
        android.apply {
            buildTypes {
                getByName("debug").matchingFallbacks += "release"
                getByName("release") {
                    isMinifyEnabled = false
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
            }
        }
    }

    private fun configureLint(android: ApplicationExtension) {
        android.lint {
            checkDependencies = true
            ignoreTestSources = true
        }
    }
}
