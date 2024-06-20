plugins {
    alias(libs.plugins.alibagherifam.android.application)
    alias(libs.plugins.alibagherifam.compose)
}

android {
    namespace = "dev.alibagherifam.mentha"
    defaultConfig {
        applicationId = "dev.alibagherifam.mentha"
        versionCode = 1
        versionName = "0.5.0"
    }
}

dependencies {
    implementation(project(":image-classifier"))
    implementation(project(":nutrition-facts"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.coil.core)
    implementation(libs.coil.compose)
    implementation(libs.accompanist.permissions)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.guava)
}
