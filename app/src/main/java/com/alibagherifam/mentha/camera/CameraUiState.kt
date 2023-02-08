package com.alibagherifam.mentha.camera

import com.alibagherifam.mentha.nutritionfacts.model.FoodEntity

data class CameraUiState(
    val food: FoodEntity? = null,
    val isFlashlightSupported: Boolean = false,
    val isFlashlightEnabled: Boolean = false
)
