package dev.alibagherifam.mentha.camera

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.view.PreviewView
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import dev.alibagherifam.mentha.R
import dev.alibagherifam.mentha.comoon.provideCameraViewModelFactory
import dev.alibagherifam.mentha.details.FoodDetailsActivity
import dev.alibagherifam.mentha.nutritionfacts.model.FoodEntity
import dev.alibagherifam.mentha.permission.RequestPermissionScaffold
import dev.alibagherifam.mentha.theme.AppTheme
import kotlinx.coroutines.delay
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
                RequestPermissionScaffold(
                    permission = Manifest.permission.CAMERA,
                    rationaleDialogTitle = stringResource(R.string.label_camera_permission),
                    rationaleDialogMessage = stringResource(R.string.message_camera_permission_required),
                    onPermissionDeny = { finish() }
                ) {
                    val screenState by viewModel.uiState.collectAsState()
                    CameraScreen(
                        screenState,
                        onFlashlightToggle = ::toggleFlashlight,
                        onSettingsClick = { },
                        onShowDetailsClick = ::openFoodDetails,
                        onPreviewViewCreated = ::startFoodRecognition
                    )
                }
            }
        }
    }

    private fun startFoodRecognition(viewFinder: PreviewView) {
        lifecycleScope.launch {
            // TODO: This delay is a workaround for unknown crash
            delay(1000)

            camera = setupCamera(
                viewFinder,
                ImageAnalyzer(viewModel.classifyImageChannel),
                lifecycleOwner = this@CameraActivity
            )

            viewModel.setFlashlightSupported(camera.cameraInfo.hasFlashUnit())
        }
    }

    private fun openFoodDetails() {
        val food = viewModel.uiState.value.food ?: return
        val i = Intent(this, FoodDetailsActivity::class.java)
        i.putExtra(FoodEntity::id.name, food.id)
        startActivity(i)
    }

    private fun toggleFlashlight(isEnabled: Boolean) {
        viewModel.setFlashlightEnabled(isEnabled)
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
