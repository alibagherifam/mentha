plugins {
    id("dev.alibagherifam.mentha.android.application")
    id("dev.alibagherifam.mentha.compose")
}

android {
    namespace = "dev.alibagherifam.mentha"
    defaultConfig {
        applicationId = "dev.alibagherifam.mentha"
        versionCode = 1
        versionName = "0.4.0"
    }
}

dependencies {
    implementation(project(":image-classifier"))
    implementation(project(":nutrition-facts"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.coil.core)
    implementation(libs.coil.compose)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.guava)
}
