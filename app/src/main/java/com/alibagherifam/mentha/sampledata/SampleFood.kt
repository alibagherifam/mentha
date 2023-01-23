package com.alibagherifam.mentha.sampledata

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.alibagherifam.mentha.R
import com.alibagherifam.mentha.nutritionfacts.model.FoodEntity
import com.alibagherifam.mentha.nutritionfacts.model.NutritionFacts

@Composable
fun getSampleFood() = FoodEntity(
    id = "orange",
    name = stringResource(R.string.sample_data_food_name),
    summary = stringResource(R.string.sample_data_food_summary),
    image = "nutrition-facts/src/main/assets/image/img_orange.png",
    nutritionFacts = NutritionFacts(
        servingWeight = 140,
        servingSize = stringResource(R.string.sample_data_serving_size),
        energy = 65.8f,
        water = 121f,
        protein = 1.27f,
        carbohydrate = 16.5f,
        fat = 0.21f,
        dietaryFiber = 2.8f,
        sugar = 12f,
        sodium = 12.6f
    )
)
