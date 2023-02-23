package dev.alibagherifam.mentha.permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat

class CameraPermissionStateHolder(context: Context) {
    private val activity: Activity = context.findActivity()
    lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private val _state: MutableState<PermissionState> = kotlin.run {
        val isPermissionGranted = ContextCompat.checkSelfPermission(
            activity, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        val permissionState = reduce(isRequested = false, isPermissionGranted)

        mutableStateOf(permissionState)
    }
    val state: State<PermissionState> get() = _state

    fun launchPermissionRequest() {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    fun onPermissionResult(isGranted: Boolean) {
        _state.value = reduce(isRequested = true, isGranted)
    }

    private fun reduce(
        isRequested: Boolean,
        isGranted: Boolean
    ): PermissionState {
        val shouldShowRationale = !isGranted && activity
            .shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
        return when {
            isGranted -> PermissionState.GRANTED
            shouldShowRationale -> PermissionState.SHOULD_SHOW_RATIONALE
            isRequested -> PermissionState.NEVER_ASK_AGAIN
            else -> PermissionState.NOT_REQUESTED
        }
    }
}
