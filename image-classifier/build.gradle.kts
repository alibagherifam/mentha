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

// TODO: Place custom model in the "assets" directory and remove download task.
project.ext.set("ASSET_DIR", "$projectDir/src/main/assets/")

fun Download.tfModel(pathOnRepository: String, saveAs: String) {
    val assetDirectory = ext.get("ASSET_DIR") as String
    src("https://tfhub.dev/$pathOnRepository?lite-format=tflite")
    dest("${assetDirectory}models/$saveAs.tflite")
    overwrite(false)
}

tasks.register<Download>("downloadMLModel") {
    tfModel(
        pathOnRepository = "iree/lite-model/mobilenet_v3_large_100_224/uint8/1",
        saveAs = "mobilenet_v3"
    )
}

tasks.register<Download>("downloadMLModel2") {
    tfModel(
        pathOnRepository = "tensorflow/lite-model/efficientnet/lite4/int8/2",
        saveAs = "efficientnet_lite4"
    )
}

tasks.preBuild.apply {
    dependsOn("downloadMLModel")
    dependsOn("downloadMLModel2")
}
