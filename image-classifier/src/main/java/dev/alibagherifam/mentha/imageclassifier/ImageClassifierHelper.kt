package dev.alibagherifam.mentha.imageclassifier

import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
import android.view.Surface
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class ImageClassifierHelper(context: Context) {
    private var imageClassifier: ImageClassifier = kotlin.run {
        val options = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(THRESHOLD)
            .setMaxResults(MAX_RESULT)
            .setLabelAllowList(foods)
            .setBaseOptions(getExecutorOptions(PROCESSOR_CPU))
            .build()

        try {
            ImageClassifier.createFromFileAndOptions(
                context, getModelPath(MODEL_MOBILENET_V3), options
            )
        } catch (e: IllegalStateException) {
            throw IllegalStateException("TFLite failed to load model with error: ${e.message}")
        }
    }

    private fun getExecutorOptions(processorType: Int): BaseOptions {
        val baseOptionsBuilder = BaseOptions.builder()
            .setNumThreads(NUM_THREADS)

        when (processorType) {
            PROCESSOR_CPU -> {
                // Default
            }
            PROCESSOR_GPU -> {
                if (CompatibilityList().isDelegateSupportedOnThisDevice) {
                    baseOptionsBuilder.useGpu()
                } else {
                    throw IllegalStateException("GPU is not supported on this device")
                }
            }
            PROCESSOR_NNAPI -> {
                baseOptionsBuilder.useNnapi()
            }
        }

        return baseOptionsBuilder.build()
    }

    private fun getModelPath(model: Int): String {
        val modelName = when (model) {
            MODEL_MOBILENET_V3 -> "mobilenet_v3"
            MODEL_EFFICIENTNET_V4 -> "efficientnet_lite4"
            else -> throw IllegalStateException()
        }
        return "models/$modelName.tflite"
    }

    fun classify(image: Bitmap, rotation: Int): Pair<List<Classifications>?, Long> {
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
        return results to inferenceTime
    }

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
        const val THRESHOLD: Float = 5.5f

        const val PROCESSOR_CPU = 0
        const val PROCESSOR_GPU = 1
        const val PROCESSOR_NNAPI = 2

        const val MODEL_MOBILENET_V3 = 0
        const val MODEL_EFFICIENTNET_V4 = 1

        // TODO: Remove this after changing model with a new one that only contains food labels
        private val foods = listOf(
            "banana",
            "broccoli",
            "cucumber",
            "lemon",
            "orange",
            "pineapple",
            "pomegranate",
            "strawberry",
            "mushroom",
            "French loaf",
            "Granny Smith",
            "bell pepper",
            "cauliflower",
            "head cabbage",
            "fig",
            "zucchini",
            "bagel",
            "pizza",
            "hot dog",
            "cheeseburger",
            "mashed potato",
            "espresso",
            "chocolate sauce",
            "butternut squash",
            "ice cream",
            "carbonara"
        )
    }
}
