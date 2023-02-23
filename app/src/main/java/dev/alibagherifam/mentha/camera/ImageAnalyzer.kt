package dev.alibagherifam.mentha.camera

import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import kotlinx.coroutines.channels.SendChannel

class ImageAnalyzer(
    private val classifierInput: SendChannel<RotatedBitmap>
) : ImageAnalysis.Analyzer {

    private lateinit var bitmap: Bitmap

    override fun analyze(image: ImageProxy) {
        if (!::bitmap.isInitialized) {
            // The RGB image buffer are initialized only once
            // the analyzer has started running
            bitmap = Bitmap.createBitmap(
                image.width, image.height, Bitmap.Config.ARGB_8888
            )
        }

        image.use {
            // Copy out RGB bits to the shared bitmap buffer
            bitmap.copyPixelsFromBuffer(it.planes[0].buffer)

            // TODO: should it be replaced with getScreenOrientation() ?
            val rotation = it.imageInfo.rotationDegrees
            val rotatedBitmap = bitmap to rotation
            classifierInput.trySend(rotatedBitmap)

            Log.i("mentha_debug", "Image sent for classification")
        }
    }
}

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
