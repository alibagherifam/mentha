plugins {
    id("com.alibagherifam.mentha.android.application")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.alibagherifam.mentha"
    defaultConfig {
        applicationId = "com.alibagherifam.mentha"
        versionCode = 1
        versionName = "1.0.0"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    flavorDimensions += "tfliteInference"
    productFlavors {
        // The TFLite inference is built using the TFLite Support library.
        create("support") {
            dimension = "tfliteInference"
        }
        // Default: The TFLite inference is built using the TFLite Task library (high-level API).
        create("taskApi") {
            dimension = "tfliteInference"
            isDefault = true
        }
    }
}

dependencies {
    "supportImplementation"(project(":lib-support"))
    "taskApiImplementation"(project(":lib-task-api"))

    implementation(libs.androidx.activity)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.coil)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.guava)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.materialDesign)
}
