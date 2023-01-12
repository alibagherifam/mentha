package com.alibagherifam.mentha.business

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.vision.classifier.Classifications

class ImageClassifier(
    context: Context,
    private val onRecognition: (Category?) -> Unit
) : ImageAnalysis.Analyzer, ImageClassifierHelper.ClassifierListener {

    private val classifier = ImageClassifierHelper(
        threshold = 0.55f,
        numThreads = 2,
        maxResults = 1,
        currentDelegate = ImageClassifierHelper.DELEGATE_CPU,
        currentModel = ImageClassifierHelper.MODEL_MOBILENETV1,
        context,
        imageClassifierListener = this
    )

    var pauseAnalysis: Boolean = false
    private lateinit var bitmapBuffer: Bitmap
    private var imageRotationDegrees: Int = 0

    override fun analyze(image: ImageProxy) {
        if (!::bitmapBuffer.isInitialized) {
            // The image rotation and RGB image buffer are initialized only once
            // the analyzer has started running
            imageRotationDegrees = image.imageInfo.rotationDegrees
            bitmapBuffer = Bitmap.createBitmap(
                image.width, image.height, Bitmap.Config.ARGB_8888
            )
        }

        // Early exit: image analysis is in paused state
        if (pauseAnalysis) {
            image.close()
            return
        }

        // Copy out RGB bits to the shared bitmap buffer
        image.use { bitmapBuffer.copyPixelsFromBuffer(image.planes[0].buffer) }

        // Perform the image classification for the current frame
        classifier.classify(bitmapBuffer, imageRotationDegrees)
    }

    override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
        onRecognition(
            results?.firstOrNull()?.categories
                ?.minByOrNull { category -> category.index }
        )
    }

    override fun onError(error: String) {
        TODO("Not yet implemented")
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
