package com.alibagherifam.mentha.permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.alibagherifam.mentha.camera.CameraPermissionRationaleDialog

@Composable
fun CameraPermissionScreen(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val context = LocalContext.current
    var cameraPermissionState by remember {
        val isCameraPermissionGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        val initialPermissionState = PermissionState.of(
            context.findActivity(),
            isRequested = false,
            isGranted = isCameraPermissionGranted
        )
        mutableStateOf(initialPermissionState)
    }
    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            cameraPermissionState = PermissionState.of(
                context.findActivity(),
                isRequested = true,
                isGranted = isGranted
            )
        }
    )

    fun requestCameraPermission() {
        requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    when (cameraPermissionState) {
        PermissionState.NOT_REQUESTED -> {
            Surface { requestCameraPermission() }
        }
        PermissionState.GRANTED -> onPermissionGranted()
        PermissionState.SHOULD_SHOW_RATIONALE -> {
            CameraPermissionRationaleDialog(
                onConfirmClick = ::requestCameraPermission,
                onDismissRequest = onPermissionDenied
            )
        }
        PermissionState.NEVER_ASK_AGAIN -> onPermissionDenied()
    }
}

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}
