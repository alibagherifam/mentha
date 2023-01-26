package com.alibagherifam.mentha.camera

import android.content.Context
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.UseCaseGroup
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.guava.await
import java.util.concurrent.Executors

suspend fun setupCamera(
    viewFinder: PreviewView,
    imageAnalyzer: ImageAnalysis.Analyzer,
    lifecycleOwner: LifecycleOwner
): Camera {
    val useCases = UseCaseGroup.Builder()
        .addUseCase(getPreviewUseCase(viewFinder))
        .addUseCase(getImageAnalysisUseCase(viewFinder, imageAnalyzer))
        .build()
    return bindCameraUseCases(
        viewFinder.context,
        lifecycleOwner,
        useCases
    )
}

fun getPreviewUseCase(viewFinder: PreviewView) =
    Preview.Builder()
        .setTargetAspectRatio(AspectRatio.RATIO_4_3)
        .setTargetRotation(viewFinder.display.rotation)
        .build().apply {
            // Attach the viewfinder's surface provider to preview use case
            // Todo: Sample calls this after binding use-cases
            setSurfaceProvider(viewFinder.surfaceProvider)
        }

fun getImageAnalysisUseCase(
    viewFinder: PreviewView,
    imageAnalyzer: ImageAnalysis.Analyzer
) = ImageAnalysis.Builder()
    .setTargetAspectRatio(AspectRatio.RATIO_4_3)
    .setTargetRotation(viewFinder.display.rotation)
    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
    .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
    .build().apply {
        setAnalyzer(Executors.newSingleThreadExecutor(), imageAnalyzer)
    }

suspend fun bindCameraUseCases(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    useCases: UseCaseGroup
): Camera {
    val cameraProvider = ProcessCameraProvider.getInstance(context).await()
    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    // Unbind use-cases before rebinding
    cameraProvider.unbindAll()

    return cameraProvider.bindToLifecycle(
        lifecycleOwner,
        cameraSelector,
        useCases
    )
}
