package dev.alibagherifam.mentha.comoon

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import dev.alibagherifam.mentha.camera.CameraViewModel
import dev.alibagherifam.mentha.details.FoodDetailsViewModel
import dev.alibagherifam.mentha.imageclassifier.ImageClassifierHelper
import dev.alibagherifam.mentha.nutritionfacts.provideFoodRepository

fun provideImageClassifier(context: Context) = ImageClassifierHelper(context)

fun provideStringProvider(context: Context) = StringProvider(context.resources)

fun provideFormatFloatValueUseCase(context: Context) =
    FormatFloatValueUseCase(provideStringProvider(context))

fun provideFormatNutritionWeightUseCase(context: Context) =
    FormatNutritionWeightUseCase(provideFormatFloatValueUseCase(context))

fun provideFormatEnergyUseCase(context: Context) =
    FormatEnergyUseCase(provideFormatFloatValueUseCase(context))

fun provideCameraViewModelFactory(context: Context): ViewModelProvider.Factory =
    CameraViewModel.Provider(
        provideImageClassifier(context),
        provideFoodRepository(context)
    )

fun provideFoodDetailsViewModelFactory(
    context: Context,
    foodId: String
): ViewModelProvider.Factory = FoodDetailsViewModel.Provider(
    foodId,
    provideFoodRepository(context),
    provideFormatEnergyUseCase(context),
    provideFormatNutritionWeightUseCase(context),
    provideStringProvider(context)
)
