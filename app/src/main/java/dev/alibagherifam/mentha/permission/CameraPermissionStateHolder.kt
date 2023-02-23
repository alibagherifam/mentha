package dev.alibagherifam.mentha.permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

class CameraPermissionStateHolder(
    private val context: Context,
) {
    lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private val _state: MutableState<PermissionState> = kotlin.run {
        val isCameraPermissionGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        val permissionState = PermissionState.of(
            context.findActivity(),
            isRequested = false,
            isGranted = isCameraPermissionGranted
        )

        mutableStateOf(permissionState)
    }
    val state: State<PermissionState> get() = _state

    fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    fun onRequestResult(isGranted: Boolean) {
        _state.value = PermissionState.of(
            context.findActivity(),
            isRequested = true,
            isGranted = isGranted
        )
    }
}

@Composable
fun rememberCameraPermissionStateHolder(): CameraPermissionStateHolder {
    val context = LocalContext.current
    val stateHolder = remember { CameraPermissionStateHolder(context) }
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = stateHolder::onRequestResult
    )
    stateHolder.requestPermissionLauncher = requestPermissionLauncher
    return stateHolder
}

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}
