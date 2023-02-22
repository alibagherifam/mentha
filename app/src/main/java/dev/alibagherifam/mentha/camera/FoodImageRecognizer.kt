package dev.alibagherifam.mentha.camera

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import dev.alibagherifam.mentha.imageclassifier.ImageClassifierHelper
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.vision.classifier.Classifications

class FoodImageRecognizer(context: Context) : ImageAnalysis.Analyzer {

    private lateinit var bitmapBuffer: Bitmap
    private var imageRotationDegrees: Int = 0

    private val classifier = ImageClassifierHelper(
        context,
        threshold = 0.55f,
        numOfThreads = 2,
        maxResults = 1,
        processorType = ImageClassifierHelper.PROCESSOR_CPU,
        model = ImageClassifierHelper.MODEL_MOBILENET_V3
    )

    private val recognitionChannel = Channel<Bitmap>(capacity = Channel.CONFLATED)
    val recognizedFoodLabels: Flow<String?> = recognitionChannel
        .consumeAsFlow()
        .map { bitmap ->
            val (results, inferenceTime) = classifier.classify(bitmap, imageRotationDegrees)
            Log.i(TAG, "Inference Time: $inferenceTime")
            results?.firstOutput()?.mostAccurateOne()?.label
        }
        .filter { it == null || it in foods }
        .distinctUntilChanged()

    override fun analyze(image: ImageProxy) {
        if (!::bitmapBuffer.isInitialized) {
            // The image rotation and RGB image buffer are initialized only once
            // the analyzer has started running
            imageRotationDegrees = image.imageInfo.rotationDegrees
            bitmapBuffer = Bitmap.createBitmap(
                image.width, image.height, Bitmap.Config.ARGB_8888
            )
        }

        Log.i(TAG, "Image width: ${image.width}, height: ${image.height}")

        // Copy out RGB bits to the shared bitmap buffer
        image.use { bitmapBuffer.copyPixelsFromBuffer(image.planes[0].buffer) }

        recognitionChannel.trySend(bitmapBuffer)
    }

    // Our model has single output so we are only interested in
    // the first output from classification results.
    private fun List<Classifications>.firstOutput(): List<Category>? =
        this.firstOrNull()?.categories

    private fun List<Category>.mostAccurateOne(): Category? =
        this.minByOrNull { category -> category.index }

    // TODO: Remove this after changing model with a new one that only contains food labels
    companion object {
        private const val TAG = "FOOD_RECOGNIZER"
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


/*override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    val imageAnalyzer: ImageAnalysis? = null
    imageAnalyzer?.targetRotation = binding.viewFinder.display.rotation
}*/

/*
private fun getScreenOrientation() : Int {
    val outMetrics = DisplayMetrics()

    val display: Display?
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
        display = requireActivity().display
        display?.getRealMetrics(outMetrics)
    } else {
        @Suppress("DEPRECATION")
        display = requireActivity().windowManager.defaultDisplay
        @Suppress("DEPRECATION")
        display.getMetrics(outMetrics)
    }

    return display?.rotation ?: 0
}
*/
