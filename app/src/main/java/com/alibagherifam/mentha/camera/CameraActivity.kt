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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
    private val food = recognitionChannel.consumeAsFlow().map { recognition ->
        recognition?.label?.let { label -> repository.getFood(label) }
    }.stateIn(
        lifecycleScope,
        started = SharingStarted.Lazily,
        initialValue = null
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val food by food.collectAsState()
                CameraScreen(
                    food,
                    isFlashlightOn = false,
                    onFlashlightToggle = { isChecked ->
                        camera.cameraControl.enableTorch(isChecked)
                    },
                    onSettingsClick = { },
                    onShowDetailsClick = {
                        food?.let { openFoodDetails(it) }
                    },
                    imageAnalyzer = ImageAnalyzer(this, recognitionChannel)
                )
            }
        }

        if (hasCameraPermission()) {
//            bindCameraUseCases()
        } else {
            requestCameraPermission()
        }
    }

    private fun openFoodDetails(food: FoodEntity) {
        val i = Intent(this, FoodDetailsActivity::class.java)
        i.putExtra(FoodEntity::id.name, food.id)
        startActivity(i)
    }

    private fun hasCameraPermission() = ContextCompat
        .checkSelfPermission(this, CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
//            bindCameraUseCases()
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
