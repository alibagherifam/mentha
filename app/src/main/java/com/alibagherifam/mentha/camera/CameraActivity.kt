package com.alibagherifam.mentha.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.alibagherifam.mentha.R
import com.alibagherifam.mentha.details.FoodDetailsActivity
import com.alibagherifam.mentha.nutritionfacts.FoodRepository
import com.alibagherifam.mentha.nutritionfacts.model.FoodEntity
import com.alibagherifam.mentha.nutritionfacts.provideFoodRepository
import com.alibagherifam.mentha.theme.AppTheme
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.label.Category

class CameraActivity : AppCompatActivity() {

    companion object {
        private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
    }

    private val recognitionChannel = Channel<Category?>(capacity = Channel.CONFLATED)
    private val repository: FoodRepository by lazy {
        provideFoodRepository(this)
    }
    private lateinit var camera: Camera
    private val uiState = MutableStateFlow(CameraUiState())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch { collectFoodRecognitions() }
        uiState.update { it.copy(isCameraPermissionGranted = hasCameraPermission()) }
        setContent {
            AppTheme {
                val state by uiState.collectAsState()
                if (state.isCameraPermissionGranted) {
                    CameraScreen(
                        state,
                        imageAnalyzer = ImageAnalyzer(this, recognitionChannel),
                        onFlashlightToggle = ::toggleFlashlight,
                        onSettingsClick = { },
                        onShowDetailsClick = ::openFoodDetails
                    )
                } else {
                    requestCameraPermission()
                }
            }
        }
    }

    private suspend fun collectFoodRecognitions() {
        recognitionChannel.consumeAsFlow()
            .map { recognition ->
                recognition?.label?.let { repository.getFood(it) }
            }.collect { recognizedFood ->
                uiState.update { it.copy(food = recognizedFood) }
            }
    }

    private fun openFoodDetails() {
        val food = uiState.value.food ?: return
        val i = Intent(this, FoodDetailsActivity::class.java)
        i.putExtra(FoodEntity::id.name, food.id)
        startActivity(i)
    }

    private fun toggleFlashlight(isEnabled: Boolean) {
        uiState.update { it.copy(isFlashlightEnabled = isEnabled) }

        // TODO: Uncomment after storing bound camera in camera variable
        // camera.cameraControl.enableTorch(isEnabled)
    }

    private fun hasCameraPermission() = ContextCompat
        .checkSelfPermission(this, CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            uiState.update { it.copy(isCameraPermissionGranted = true) }
        } else {
            finish()
        }
    }

    private fun requestCameraPermission() {
        if (shouldShowRequestPermissionRationale(CAMERA_PERMISSION)) {
            Toast.makeText(
                this,
                getString(R.string.message_camera_permission_required),
                Toast.LENGTH_LONG
            ).show()
        }
        requestPermissionsLauncher.launch(CAMERA_PERMISSION)
    }
}
