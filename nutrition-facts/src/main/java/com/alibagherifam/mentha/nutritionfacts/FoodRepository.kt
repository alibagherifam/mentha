package com.alibagherifam.mentha.nutritionfacts

import androidx.annotation.DrawableRes
import com.alibagherifam.mentha.nutritionfacts.model.FoodEntity

class FoodRepository(private val localDataSource: FoodDao) {
    suspend fun getFood(id: String): FoodEntity =
        localDataSource.getFood(id).copy(icon = getFoodIcon(id))

    @DrawableRes
    private fun getFoodIcon(foodId: String): Int {
        return when (foodId) {
            "banana" -> R.drawable.img_banana
            "broccoli" -> R.drawable.img_broccoli
            "cucumber" -> R.drawable.img_cucumber
            "lemon" -> R.drawable.img_lemon
            "orange" -> R.drawable.img_orange
            "pineapple" -> R.drawable.img_pineapple
            "pomegranate" -> R.drawable.img_pomegranate
            "strawberry" -> R.drawable.img_strawberry
            "mushroom" -> R.drawable.img_mushroom
            "French loaf" -> R.drawable.img_baguette
            else -> 1
        }
    }
}
