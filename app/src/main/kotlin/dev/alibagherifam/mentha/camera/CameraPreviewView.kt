package dev.alibagherifam.mentha.camera

import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CameraPreviewView(
    onPreviewViewCreated: ((PreviewView) -> Unit)?,
    modifier: Modifier = Modifier
) {
    // Only in compose preview
    if (onPreviewViewCreated == null) {
        Surface(modifier = modifier.fillMaxSize(), color = Color.Black) {}
    } else {
        AndroidView(
            modifier = modifier.fillMaxSize(),
            factory = { context -> PreviewView(context) },
            update = { onPreviewViewCreated(it) }
        )
    }
}
