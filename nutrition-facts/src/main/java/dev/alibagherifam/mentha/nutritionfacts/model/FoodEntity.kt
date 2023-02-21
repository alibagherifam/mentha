package dev.alibagherifam.mentha.nutritionfacts.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food")
data class FoodEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val summary: String,
    val image: String,
    @Embedded
    val nutritionFacts: NutritionFacts
)
