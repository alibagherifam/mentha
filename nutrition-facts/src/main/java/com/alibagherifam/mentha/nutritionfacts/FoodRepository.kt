package com.alibagherifam.mentha.nutritionfacts

import com.alibagherifam.mentha.nutritionfacts.model.FoodEntity

class FoodRepository(private val localDataSource: FoodDao) {
    suspend fun getFood(id: String): FoodEntity = localDataSource.getFood(id)
}
