plugins {
    id("com.alibagherifam.mentha.android.library")
}

android.namespace = "com.alibagherifam.mentha.classifier"

dependencies {
    implementation(project(":models"))
    implementation(libs.androidx.appcompat)
    implementation(libs.tensorflow.taskVision)
    implementation(libs.tensorflow.metadata)
    implementation(libs.tensorflow.gpuDelegate)
}
