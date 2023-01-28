package com.alibagherifam.mentha.camera

import com.alibagherifam.mentha.nutritionfacts.model.FoodEntity

data class CameraUiState(
    val food: FoodEntity? = null,
    val isCameraPermissionGranted: Boolean = false,
    val isFlashlightSupported: Boolean = false,
    val isFlashlightEnabled: Boolean = false
)
