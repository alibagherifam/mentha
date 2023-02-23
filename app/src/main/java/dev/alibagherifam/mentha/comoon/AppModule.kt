package dev.alibagherifam.mentha.comoon

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import dev.alibagherifam.mentha.camera.CameraViewModel
import dev.alibagherifam.mentha.details.FoodDetailsViewModel
import dev.alibagherifam.mentha.imageclassifier.ImageClassifierHelper
import dev.alibagherifam.mentha.nutritionfacts.provideFoodRepository

fun provideCameraViewModelFactory(context: Context): ViewModelProvider.Factory =
    CameraViewModel.Provider(
        provideImageClassifier(context),
        provideFoodRepository(context)
    )

fun provideImageClassifier(context: Context) = ImageClassifierHelper(context)

fun provideFoodDetailsViewModelFactory(
    foodId: String, context: Context
): ViewModelProvider.Factory = FoodDetailsViewModel.Provider(
    foodId = foodId,
    repository = provideFoodRepository(context),
    stringProvider = StringProvider(context.resources)
)
