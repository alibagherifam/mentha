plugins {
    id("com.alibagherifam.mentha.android.library")
}

android.namespace = "com.alibagherifam.mentha.classifier"

dependencies {
    implementation(project(":models"))
    implementation(libs.androidx.appcompat)
    implementation(libs.tensorflow.core)
    implementation(libs.tensorflow.gpu)
    implementation(libs.tensorflow.support)

    // Use local TensorFlow library
    // implementation(libs.tensorflow.local)
}
