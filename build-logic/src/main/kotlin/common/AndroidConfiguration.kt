package common

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.kotlin.dsl.dependencies

fun Project.configureAndroidBaseOptions(android: CommonExtension<*, *, *, *>) {
    val libs = getVersionCatalogs()
    setSdkVersionBoundary(android, libs)
    setJvmTargetVersion(android)
    // desugarJdkLibraries(android, libs)
    configureWithDetekt(libs)
    excludeLicencesFromApk(android)
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

fun Project.desugarJdkLibraries(
    android: CommonExtension<*, *, *, *>,
    libs: VersionCatalog
) {
    android.compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }

    dependencies {
        add(
            "coreLibraryDesugaring",
            libs.findLibrary("android.desugarJdkLibs").get()
        )
    }
}

private fun excludeLicencesFromApk(android: CommonExtension<*, *, *, *>) {
    android.packaging.resources {
        excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}
