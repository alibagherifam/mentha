package common

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project

fun Project.configureAndroidBaseOptions(android: CommonExtension<*, *, *, *>) {
    setSdkVersionBoundary(android)
    setJvmTargetVersion(android)
}

fun setSdkVersionBoundary(android: CommonExtension<*, *, *, *>) {
    android.apply {
        compileSdk = 33
        buildToolsVersion = "34.0.0-rc2"
        defaultConfig {
            minSdk = 24
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }
}
