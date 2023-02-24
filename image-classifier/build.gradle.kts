import com.android.build.gradle.internal.tasks.factory.dependsOn
import de.undercouch.gradle.tasks.download.Download

plugins {
    id("dev.alibagherifam.mentha.android.library")
    alias(libs.plugins.gradleDownload)
}

android {
    namespace = "dev.alibagherifam.mentha.imageclassifier"

    android.androidResources {
        noCompress("tflite")
    }
}

dependencies {
    implementation(libs.tensorflow.taskVision)
}

// TODO: Place custom model in the "assets" directory and remove download task.
project.ext.set("ASSET_DIR", "$projectDir/src/main/assets/")

fun Download.tfModel(
    publisher: String,
    pathOnRepository: String,
    saveAs: String
) {
    val assetDirectory = ext.get("ASSET_DIR") as String
    src("https://tfhub.dev/$publisher/lite-model/$pathOnRepository?lite-format=tflite")
    dest("${assetDirectory}models/$saveAs.tflite")
    overwrite(false)
}

tasks.register<Download>("downloadMLModel") {
    tfModel(
        publisher = "google",
        pathOnRepository = "imagenet/mobilenet_v3_large_100_224/classification/5/metadata/1",
        saveAs = "mobilenet_v3"
    )
}

/*tasks.register<Download>("downloadMLModel") {
    tfModel(
        publisher = "tensorflow",
        pathOnRepository = "efficientnet/lite4/uint8/2",
        saveAs = "efficientnet_lite4"
    )
}*/

tasks.preBuild.apply {
    dependsOn("downloadMLModel")
}
