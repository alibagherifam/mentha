package dev.alibagherifam.mentha.camera

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import dev.alibagherifam.mentha.comoon.provideCameraViewModelFactory
import dev.alibagherifam.mentha.details.FoodDetailsActivity
import dev.alibagherifam.mentha.nutritionfacts.model.FoodEntity
import dev.alibagherifam.mentha.permission.CameraPermissionRationaleDialog
import dev.alibagherifam.mentha.permission.PermissionState.GRANTED
import dev.alibagherifam.mentha.permission.PermissionState.NEVER_ASK_AGAIN
import dev.alibagherifam.mentha.permission.PermissionState.NOT_REQUESTED
import dev.alibagherifam.mentha.permission.PermissionState.SHOULD_SHOW_RATIONALE
import dev.alibagherifam.mentha.permission.rememberPermissionStateHolder
import dev.alibagherifam.mentha.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CameraActivity : AppCompatActivity() {

    private val viewModel: CameraViewModel by viewModels {
        provideCameraViewModelFactory(this)
    }

    private lateinit var camera: Camera

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AppTheme { Content() } }
    }

    @Composable
    private fun Content() {
        val cameraPermissionStateHolder = rememberPermissionStateHolder(
            Manifest.permission.CAMERA
        )
        when (val state = cameraPermissionStateHolder.state.value) {
            NOT_REQUESTED -> SideEffect {
                cameraPermissionStateHolder.launchPermissionRequest()
            }
            GRANTED -> {
                val screenState by viewModel.uiState.collectAsState()
                CameraScreen(
                    screenState,
                    onFlashlightToggle = ::toggleFlashlight,
                    onSettingsClick = { },
                    onShowDetailsClick = ::openFoodDetails,
                    onPreviewViewCreated = ::startFoodRecognition
                )
            }
            SHOULD_SHOW_RATIONALE, NEVER_ASK_AGAIN -> {
                CameraPermissionRationaleDialog(
                    onConfirmClick = if (state == SHOULD_SHOW_RATIONALE) {
                        cameraPermissionStateHolder::launchPermissionRequest
                    } else {
                        ::openAppSettings
                    },
                    onDismissRequest = { finish() }
                )
            }
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
            addCategory(Intent.CATEGORY_DEFAULT)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        }
        startActivity(intent)
    }

    private fun startFoodRecognition(viewFinder: PreviewView) {
        lifecycleScope.launch {
            // TODO: This delay is a workaround for unknown crash
            delay(1000)

            val recognizer = FoodImageRecognizer(this@CameraActivity)
            camera = setupCamera(
                viewFinder,
                recognizer,
                lifecycleOwner = this@CameraActivity
            )

            viewModel.uiState.update {
                it.copy(isFlashlightSupported = camera.cameraInfo.hasFlashUnit())
            }

            viewModel.setImageRecognizer(recognizer)
        }
    }

    private fun openFoodDetails() {
        val food = viewModel.uiState.value.food ?: return
        val i = Intent(this, FoodDetailsActivity::class.java)
        i.putExtra(FoodEntity::id.name, food.id)
        startActivity(i)
    }

    private fun toggleFlashlight(isEnabled: Boolean) {
        viewModel.uiState.update { it.copy(isFlashlightEnabled = isEnabled) }
        camera.cameraControl.enableTorch(isEnabled)
    }
}

/*
override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    val imageAnalyzer: ImageAnalysis? = null
    imageAnalyzer?.targetRotation = binding.viewFinder.display.rotation
}
*/

