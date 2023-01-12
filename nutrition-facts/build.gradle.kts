plugins {
    id("com.alibagherifam.mentha.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.alibagherifam.mentha.nutritionfacts"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core)
    implementation(libs.kotlinx.serialization.json)
}
