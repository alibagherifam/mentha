package dev.alibagherifam.mentha.imageclassifier

import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
import android.util.Log
import android.view.Surface
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class ImageClassifierHelper(context: Context) {
    private val imageClassifier: ImageClassifier = kotlin.run {
        val options = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(THRESHOLD)
            .setMaxResults(MAX_RESULT)
            .setLabelAllowList(ALLOWED_LABELS)
            .setBaseOptions(
                BaseOptions.builder().setNumThreads(NUM_THREADS).build()
            ).build()

        val modelPath = "models/$MODEL_FILE_NAME.tflite"

        try {
            ImageClassifier.createFromFileAndOptions(context, modelPath, options)
        } catch (e: IllegalStateException) {
            error("TFLite failed to load model with error: ${e.message}")
        }
    }

    fun classify(image: Bitmap, rotation: Int): String? {
        var inferenceTime = SystemClock.uptimeMillis()

        // Create preprocessor for the image.
        // See https://www.tensorflow.org/lite/inference_with_metadata/
        //            lite_support#imageprocessor_architecture
        val imageProcessor = ImageProcessor.Builder().build()

        // Preprocess the image and convert it into a TensorImage for classification.
        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(image))

        val imageProcessingOptions = ImageProcessingOptions.builder()
            .setOrientation(getOrientationFromRotation(rotation))
            .build()

        val results = imageClassifier.classify(tensorImage, imageProcessingOptions)

        inferenceTime = SystemClock.uptimeMillis() - inferenceTime
        Log.i("mentha_debug", "Inference Time: $inferenceTime")

        return results.mostAccurateOne()?.label
    }

    // Our model has single result so we are only interested in
    // the first index from classification results.
    private fun List<Classifications>.mostAccurateOne(): Category? =
        this.firstOrNull()?.categories?.firstOrNull()

    // Receive the device rotation (Surface.x values range from 0->3) and return EXIF orientation
    // http://jpegclub.org/exif_orientation.html
    private fun getOrientationFromRotation(
        rotation: Int
    ): ImageProcessingOptions.Orientation = when (rotation) {
        Surface.ROTATION_270 -> {
            ImageProcessingOptions.Orientation.BOTTOM_RIGHT
        }
        Surface.ROTATION_180 -> {
            ImageProcessingOptions.Orientation.RIGHT_BOTTOM
        }
        Surface.ROTATION_90 -> {
            ImageProcessingOptions.Orientation.TOP_LEFT
        }
        else -> {
            ImageProcessingOptions.Orientation.RIGHT_TOP
        }
    }

    companion object {
        const val MAX_RESULT: Int = 1
        const val NUM_THREADS: Int = 2

        // MobileNet-V3
        const val MODEL_FILE_NAME = "mobilenet_v3"
        const val THRESHOLD: Float = 5.5f

        // EfficientNet-Lite4
        // const val MODEL_FILE_NAME  = "efficientnet_lite4"
        // const val THRESHOLD: Float = 0.55f
    }
}
