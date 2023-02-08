package com.alibagherifam.mentha.camera

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.view.PreviewView
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import com.alibagherifam.mentha.comoon.provideCameraViewModelFactory
import com.alibagherifam.mentha.details.FoodDetailsActivity
import com.alibagherifam.mentha.nutritionfacts.model.FoodEntity
import com.alibagherifam.mentha.permission.CameraPermissionScreen
import com.alibagherifam.mentha.theme.AppTheme
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
                var isCameraPermissionGranted by remember {
                    mutableStateOf(false)
                }
                if (isCameraPermissionGranted) {
                    val state by viewModel.uiState.collectAsState()
                    CameraScreen(
                        state,
                        onFlashlightToggle = ::toggleFlashlight,
                        onSettingsClick = { },
                        onShowDetailsClick = ::openFoodDetails,
                        onPreviewViewCreated = ::startFoodRecognition
                    )
                } else {
                    CameraPermissionScreen(
                        onPermissionGranted = { isCameraPermissionGranted = true },
                        onPermissionDenied = { finish() }
                    )
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
