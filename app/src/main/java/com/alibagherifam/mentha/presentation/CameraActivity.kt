package com.alibagherifam.mentha.presentation

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
import com.alibagherifam.mentha.business.ImageAnalyzer
import com.alibagherifam.mentha.nutritionfacts.Food
import com.alibagherifam.mentha.nutritionfacts.FoodRepository
import com.alibagherifam.mentha.theme.AppTheme
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.tensorflow.lite.support.label.Category

class CameraActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSION_CAMERA = Manifest.permission.CAMERA
    }

    private val recognitionChannel = Channel<Category?>(capacity = Channel.CONFLATED)
    private val repository by lazy {
        FoodRepository.getInstance(this)
    }
    private lateinit var camera: Camera
    private val food = recognitionChannel.consumeAsFlow().map { recognition ->
        recognition?.label?.let { label ->
            repository.foods.find { it.id == label }
        }
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

    private fun openFoodDetails(food: Food) {
        val i = Intent(this, FoodDetailsActivity::class.java)
        i.putExtra(Food::id.name, food.id)
        startActivity(i)
    }

    private fun hasCameraPermission() =
        ContextCompat.checkSelfPermission(
            this, PERMISSION_CAMERA
        ) == PackageManager.PERMISSION_GRANTED

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
        if (shouldShowRequestPermissionRationale(PERMISSION_CAMERA)) {
            Toast.makeText(
                this,
                "Camera permission is required!",
                Toast.LENGTH_LONG
            ).show()
        }
        requestPermissionsLauncher.launch(PERMISSION_CAMERA)
    }
}
