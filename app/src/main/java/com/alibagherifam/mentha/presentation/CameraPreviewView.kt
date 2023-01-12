package com.alibagherifam.mentha.presentation

import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import com.alibagherifam.mentha.business.ImageAnalyzer
import com.alibagherifam.mentha.business.setupCamera
import kotlinx.coroutines.launch

@Composable
fun CameraPreviewView(imageAnalyzer: ImageAnalyzer?) {
    // Only for preview purpose
    if (imageAnalyzer == null) {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {}
        return
    }
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val viewFinder = PreviewView(context)
            coroutineScope.launch {
                setupCamera(viewFinder, imageAnalyzer, lifecycleOwner)
            }
            viewFinder
        }
    )
}
