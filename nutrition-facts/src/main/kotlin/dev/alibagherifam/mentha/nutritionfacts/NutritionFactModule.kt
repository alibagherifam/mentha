package dev.alibagherifam.mentha.nutritionfacts

import android.content.Context

fun provideFoodDao(context: Context): FoodDao =
    FoodDatabase.getInstance(context).getFoodDao()

fun provideFoodRepository(context: Context): FoodRepository =
    FoodRepository(provideFoodDao(context))
