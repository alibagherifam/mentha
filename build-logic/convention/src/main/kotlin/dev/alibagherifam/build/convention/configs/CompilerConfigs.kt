package dev.alibagherifam.build.convention.configs

import com.android.build.api.dsl.CommonExtension
import dev.alibagherifam.build.convention.getRequiredVersion
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun CommonExtension<*, *, *, *, *, *>.configureAndroidCompilation(
    project: Project,
    libs: VersionCatalog
) {
    val targetVersion = JavaVersion.VERSION_11
    compileOptions {
        sourceCompatibility = targetVersion
        targetCompatibility = targetVersion
    }

    with(project) {
        configureKotlinCompiler()
        configureAndroidSdkVersions(libs)
        desugarJdkLibraries(project = this, libs)
    }
}

private fun CommonExtension<*, *, *, *, *, *>.configureAndroidSdkVersions(
    libs: VersionCatalog
) {
    compileSdk = libs.getRequiredVersion("androidCompileSdk").toInt()
    defaultConfig.minSdk = libs.getRequiredVersion("androidMinSdk").toInt()
}

private fun CommonExtension<*, *, *, *, *, *>.desugarJdkLibraries(
    project: Project,
    libs: VersionCatalog
) {
    with(project) {
        compileOptions.isCoreLibraryDesugaringEnabled = true

        dependencies.add(
            "coreLibraryDesugaring",
            libs.findLibrary("android.desugarJdkLibs").get()
        )
    }
}

internal fun Project.configureJvmCompilation() {
    val targetVersion = JavaVersion.VERSION_11
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = targetVersion
        targetCompatibility = targetVersion
    }

    configureKotlinCompiler()
}

private fun Project.configureKotlinCompiler() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
            optIn.addAll(
                // Enable experimental coroutines APIs, including Flow
                "kotlinx.coroutines.ExperimentalCoroutinesApi",
            )
        }
    }
}
