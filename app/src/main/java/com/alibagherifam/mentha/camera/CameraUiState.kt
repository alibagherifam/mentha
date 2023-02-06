package com.alibagherifam.mentha.camera

import com.alibagherifam.mentha.nutritionfacts.model.FoodEntity

data class CameraUiState(
    val food: FoodEntity? = null,
    val cameraPermissionState: PermissionState = PermissionState.NOT_REQUESTED,
    val isFlashlightSupported: Boolean = false,
    val isFlashlightEnabled: Boolean = false
)
