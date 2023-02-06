package com.alibagherifam.mentha.comoon

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.alibagherifam.mentha.camera.CameraViewModel
import com.alibagherifam.mentha.nutritionfacts.provideFoodRepository

fun provideCameraViewModelFactory(context: Context): ViewModelProvider.Factory =
    CameraViewModel.Provider(provideFoodRepository(context))
