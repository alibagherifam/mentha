package com.alibagherifam.mentha.business

import android.app.Activity
import android.graphics.Bitmap
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.alibagherifam.mentha.classifier.Classifier

class ImageClassifier(
    activity: Activity,
    private val onRecognition: (Classifier.Recognition?) -> Unit
) : ImageAnalysis.Analyzer {

    companion object {
        private const val ACCURACY_THRESHOLD = 0.55f
        private const val NUM_THREADS = 1
        private val CLASSIFICATION_MODEL = Classifier.Model.QUANTIZED_EFFICIENTNET
        private val PROCESSING_DEVICE = Classifier.Device.CPU
    }

    private val classifier = Classifier.create(
        activity,
        CLASSIFICATION_MODEL,
        PROCESSING_DEVICE,
        NUM_THREADS
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

        // Copy out RGB bits to our shared buffer
        image.use { bitmapBuffer.copyPixelsFromBuffer(image.planes[0].buffer) }

        // Perform the object classification for the current frame
        val recognitions = classifier.recognizeImage(bitmapBuffer, imageRotationDegrees)

        // Report only the top recognition with enough accuracy
        val acceptedRecognition =
            recognitions.maxByOrNull { it.confidence }
                ?.takeIf { it.confidence >= ACCURACY_THRESHOLD }

        onRecognition(acceptedRecognition)
    }
}
