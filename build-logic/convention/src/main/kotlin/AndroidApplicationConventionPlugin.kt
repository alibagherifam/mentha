import com.android.build.api.dsl.ApplicationExtension
import dev.alibagherifam.build.convention.configs.configureAndroidBaseOptions
import dev.alibagherifam.build.convention.getRequiredVersion
import dev.alibagherifam.build.convention.libsCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.android")
                apply("com.android.application")
            }

            extensions.configure<ApplicationExtension> {
                configureAndroidBaseOptions(this@with)
                setTargetSdkVersion(libs = libsCatalog)
//                configureSigning(this)
                configureBuildTypes(this)
            }
        }
    }

    private fun ApplicationExtension.setTargetSdkVersion(libs: VersionCatalog) {
        val versionString = libs.getRequiredVersion("androidTargetSdk")
        defaultConfig.targetSdk = versionString.toInt()
    }

    /*    private fun Project.configureSigning(android: ApplicationExtension) {
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
        }*/

    private fun configureBuildTypes(android: ApplicationExtension) {
        android.apply {
            buildTypes {
                getByName("debug").matchingFallbacks += "release"
                getByName("release") {
                    isMinifyEnabled = true
                    isShrinkResources = true
//                    signingConfig = signingConfigs.getByName("release")
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
            }
        }
    }
}
