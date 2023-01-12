package com.alibagherifam.mentha.nutritionfacts

import android.app.Activity
import androidx.annotation.DrawableRes
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class FoodRepository(activity: Activity) {

    companion object {
        private var INSTANCE: FoodRepository? = null

        fun getInstance(activity: Activity): FoodRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = FoodRepository(activity)
                INSTANCE = instance
                instance
            }
        }
    }

    val foods: List<Food> = loadFoodsFromAsset(activity)

    fun getFood(foodId: String): Food = foods.find { it.id == foodId }!!

    private fun loadFoodsFromAsset(activity: Activity): List<Food> {
        val jsonFile = activity.assets.open("foods.json")
        val foodsJson = jsonFile.bufferedReader().use { it.readText() }
        val noIconFoods: List<Food> = Json.decodeFromString(foodsJson)
        return noIconFoods.map { it.copy(icon = getFoodIcon(it)) }
    }

    @DrawableRes
    private fun getFoodIcon(food: Food): Int? {
        return when (food.id) {
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
            else -> null
        }
    }
}
