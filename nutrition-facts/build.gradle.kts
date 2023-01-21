plugins {
    id("com.alibagherifam.mentha.android.library")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.alibagherifam.mentha.nutritionfacts"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
}
