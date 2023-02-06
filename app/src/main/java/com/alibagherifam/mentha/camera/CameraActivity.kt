package com.alibagherifam.mentha.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.view.PreviewView
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.alibagherifam.mentha.comoon.provideCameraViewModelFactory
import com.alibagherifam.mentha.details.FoodDetailsActivity
import com.alibagherifam.mentha.nutritionfacts.model.FoodEntity
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
        checkForCameraPermission()
        setContent {
            AppTheme {
                val state by viewModel.uiState.collectAsState()
                if (state.isCameraPermissionGranted) {
                    CameraScreen(
                        state,
                        onFlashlightToggle = ::toggleFlashlight,
                        onSettingsClick = { },
                        onShowDetailsClick = ::openFoodDetails,
                        onPreviewViewCreated = ::startFoodRecognition
                    )
                } else if (state.shouldShowCameraPermissionRationale) {
                    CameraPermissionRationaleDialog(
                        onConfirmClick = ::requestCameraPermission,
                        onDismissRequest = this@CameraActivity::finish
                    )
                } else {
                    Surface { requestCameraPermission() }
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

            recognizer.recognizedFoodLabels.collect { foodLabel ->
                viewModel.updateFood(foodLabel)
            }
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

    private fun checkForCameraPermission() {
        requestCameraPermission()
        viewModel.uiState.update {
            it.copy(
                isCameraPermissionGranted = ContextCompat.checkSelfPermission(
                    this, Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            )
        }
    }

    private fun requestCameraPermission() =
        requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)

    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        val shouldShowRationale = !isGranted &&
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)

        viewModel.uiState.update {
            it.copy(
                isCameraPermissionGranted = isGranted,
                shouldShowCameraPermissionRationale = shouldShowRationale
            )
        }

        if (!isGranted && !shouldShowRationale) {
            finish()
        }
    }
}
