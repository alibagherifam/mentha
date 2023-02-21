import com.android.build.gradle.internal.tasks.factory.dependsOn
import de.undercouch.gradle.tasks.download.Download

plugins {
    id("dev.alibagherifam.mentha.android.library")
    alias(libs.plugins.gradleDownload)
}

android {
    namespace = "dev.alibagherifam.mentha.imageclassifier"
}

dependencies {
    implementation(libs.tensorflow.taskVision)
    implementation(libs.tensorflow.gpu)
    implementation(libs.tensorflow.gpuDelegate)
}


// Download default models; if you wish to use your own models then
// place them in the "assets" directory and comment out this line.
project.ext.set("ASSET_DIR", "$projectDir/src/main/assets/")
//apply(from = "download.gradle.kts")

fun Download.tfModel(remoteName: String, localName: String) {
    val assetDir = ext.get("ASSET_DIR") as String
    val tfLiteMimeType = ".tflite"
    val modelRepository = "https://storage.googleapis.com/download.tensorflow.org/models/tflite/task_library/image_classification/android"
    src(modelRepository + remoteName + tfLiteMimeType)
    dest(assetDir + localName + tfLiteMimeType)
    overwrite(false)
}

tasks.register<Download>("mobilenetv1") {
    tfModel(remoteName = "mobilenet_v1_1.0_224_quantized_1_metadata_1", localName = "mobilenetv1")
}

tasks.register<Download>("efficientnet0") {
    tfModel(remoteName = "efficientnet_lite0_int8_2", localName = "efficientnet-lite0")
}

tasks.register<Download>("efficientnet1") {
    tfModel(remoteName = "efficientnet_lite1_int8_2", localName = "efficientnet-lite1")
}

tasks.register<Download>("efficientnet2") {
    tfModel(remoteName = "efficientnet_lite2_int8_2", localName = "efficientnet-lite2")
}

tasks.preBuild.apply {
    dependsOn("mobilenetv1")
    dependsOn("efficientnet0")
    dependsOn("efficientnet1")
    dependsOn("efficientnet2")
}
