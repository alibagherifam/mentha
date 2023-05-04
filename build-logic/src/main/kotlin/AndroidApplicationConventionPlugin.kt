import com.android.build.api.dsl.ApplicationExtension
import common.configureAndroidBaseOptions
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

@Suppress("unused")
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.android")
                apply("com.android.application")
            }

            extensions.configure<ApplicationExtension> {
                configureAndroidBaseOptions(this)
                configureSigning(this)
                configureBuildTypes(this)
                configureLint(this)
                defaultConfig {
                    targetSdk = 33
                }
            }
        }
    }

    private fun Project.configureSigning(android: ApplicationExtension) {
        android.apply {
            val keystorePropertiesFile = rootProject.file("keystore.properties")
            val keystoreProperties = Properties()
            keystoreProperties.load(FileInputStream(keystorePropertiesFile))
            signingConfigs.create("release") {
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
            }
        }
    }

    private fun configureBuildTypes(android: ApplicationExtension) {
        android.apply {
            buildTypes {
                getByName("debug").matchingFallbacks += "release"
                getByName("release") {
                    isMinifyEnabled = true
                    isShrinkResources = true
                    signingConfig = signingConfigs.getByName("release")
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
