package dev.alibagherifam.mentha.nutritionfacts

import androidx.room.Dao
import androidx.room.Query
import dev.alibagherifam.mentha.nutritionfacts.model.FoodEntity

@Dao
interface FoodDao {
    @Query("""
        SELECT * FROM food
        WHERE id = :id
    """)
    suspend fun getFood(id: String): FoodEntity
}
