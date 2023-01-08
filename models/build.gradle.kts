import com.android.build.gradle.internal.tasks.factory.dependsOn
import de.undercouch.gradle.tasks.download.Download

plugins {
    id("com.alibagherifam.mentha.android.library")
    alias(libs.plugins.gradleDownload)
}

android.namespace = "org.tensorflow.lite.examples.classification.models"

// Download default models; if you wish to use your own models then
// place them in the "assets" directory and comment out this line.
project.ext.set("ASSET_DIR", "$projectDir/src/main/assets/")
//apply(from = "download.gradle.kts")

val tfModelRepository = "https://tfhub.dev/tensorflow/lite-model/"
val liteFormatQuery = "?lite-format=tflite"
val modelFloatDownloadUrl = "mobilenet_v1_1.0_224/1/metadata/1"
val modelQuantDownloadUrl = "mobilenet_v1_1.0_224_quantized/1/metadata/1"
val modelEfficientNetFloatDownloadUrl = "efficientnet/lite0/fp32/2"
val modelEfficientNetQuantDownloadUrl = "efficientnet/lite0/int8/2"

val tfLiteMimeType = ".tflite"
val modelFloatFileName = "mobilenet_v1_1.0_224"
val modelQuantFileName = "mobilenet_v1_1.0_224_quant"
val modelEfficientNetFloatFileName = "efficientnet-lite0-fp32"
val modelEfficientNetQuantFileName = "efficientnet-lite0-int8"

fun Project.assetDirectory(): String =
    this.ext.get("ASSET_DIR") as String

tasks.register<Download>("downloadModelFloat") {
    src(tfModelRepository + modelFloatDownloadUrl + liteFormatQuery)
    dest(assetDirectory() + modelFloatFileName + tfLiteMimeType)
    overwrite(false)
}

tasks.register<Download>("downloadModelQuant") {
    src(tfModelRepository + modelQuantDownloadUrl + liteFormatQuery)
    dest(assetDirectory() + modelQuantFileName + tfLiteMimeType)
    overwrite(false)
}

tasks.register<Download>("downloadEfficientNetFloat") {
    src(tfModelRepository + modelEfficientNetFloatDownloadUrl + liteFormatQuery)
    dest(assetDirectory() + modelEfficientNetFloatFileName + tfLiteMimeType)
    overwrite(false)
}

tasks.register<Download>("downloadEfficientNetQuant") {
    src(tfModelRepository + modelEfficientNetQuantDownloadUrl + liteFormatQuery)
    dest(assetDirectory() + modelEfficientNetQuantFileName + tfLiteMimeType)
    overwrite(false)
}

tasks.preBuild.apply {
    dependsOn("downloadModelFloat")
    dependsOn("downloadModelQuant")
    dependsOn("downloadEfficientNetFloat")
    dependsOn("downloadEfficientNetQuant")
}
