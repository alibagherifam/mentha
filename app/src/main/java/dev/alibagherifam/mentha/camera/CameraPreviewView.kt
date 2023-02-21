package dev.alibagherifam.mentha.camera

import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CameraPreviewView(onPreviewViewCreated: ((PreviewView) -> Unit)?) {
    // Only in compose preview
    if (onPreviewViewCreated == null) {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {}
        return
    }
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context -> PreviewView(context) },
        update = { onPreviewViewCreated(it) }
    )
}
