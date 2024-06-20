plugins {
    alias(libs.plugins.alibagherifam.android.library)
    alias(libs.plugins.ksp)
}

android {
    namespace = "dev.alibagherifam.mentha.nutritionfacts"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
}
