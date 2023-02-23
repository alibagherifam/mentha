package dev.alibagherifam.mentha.camera

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.view.PreviewView
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import dev.alibagherifam.mentha.comoon.provideCameraViewModelFactory
import dev.alibagherifam.mentha.details.FoodDetailsActivity
import dev.alibagherifam.mentha.nutritionfacts.model.FoodEntity
import dev.alibagherifam.mentha.permission.PermissionState
import dev.alibagherifam.mentha.permission.rememberCameraPermissionStateHolder
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
        setContent {
            AppTheme {
                val cameraPermissionStateHolder = rememberCameraPermissionStateHolder()
                when (cameraPermissionStateHolder.state.value) {
                    PermissionState.NOT_REQUESTED -> {
                        Surface {
                            SideEffect {
                                cameraPermissionStateHolder.requestPermission()
                            }
                        }
                    }
                    PermissionState.GRANTED -> {
                        val state by viewModel.uiState.collectAsState()
                        CameraScreen(
                            state,
                            onFlashlightToggle = ::toggleFlashlight,
                            onSettingsClick = { },
                            onShowDetailsClick = ::openFoodDetails,
                            onPreviewViewCreated = ::startFoodRecognition
                        )
                    }
                    PermissionState.SHOULD_SHOW_RATIONALE -> {
                        CameraPermissionRationaleDialog(
                            onConfirmClick = cameraPermissionStateHolder::requestPermission,
                            onDismissRequest = { finish() }
                        )
                    }
                    PermissionState.NEVER_ASK_AGAIN -> {
                        // TODO: Show a message explaining why the app can not run
                        finish()
                    }
                }
            }
        }
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

