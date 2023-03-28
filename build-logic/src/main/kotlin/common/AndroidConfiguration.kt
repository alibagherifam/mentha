package common

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog

fun Project.configureAndroidBaseOptions(android: CommonExtension<*, *, *, *>) {
    val libs = getVersionCatalogs()
    setSdkVersionBoundary(android, libs)
    setJvmTargetVersion(android)
    configureWithDetekt(libs)
}

fun setSdkVersionBoundary(android: CommonExtension<*, *, *, *>, libs: VersionCatalog) {
    android.apply {
        compileSdk = libs.getRequiredVersion("androidCompileSdk").toInt()
        buildToolsVersion = libs.getRequiredVersion("androidBuildTools")
        defaultConfig {
            minSdk = libs.getRequiredVersion("androidMinSdk").toInt()
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }
}
