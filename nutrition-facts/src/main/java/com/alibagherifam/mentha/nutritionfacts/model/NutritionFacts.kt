package com.alibagherifam.mentha.nutritionfacts.model

import androidx.room.ColumnInfo

/**
 * Value object (composite attribute) for nutrition facts
 *
 * @param servingSize: how the food is measured (cup, tbsp, etc.)
 * @param servingWeight: weight of serving size
 * @param energy: energy in kilo calorie
 */
data class NutritionFacts(
    @ColumnInfo("serving_size")
    val servingSize: String,
    @ColumnInfo("serving_weight")
    val servingWeight: Int,
    val energy: Float,
    val water: Float,
    val protein: Float,
    val carbohydrate: Float,
    val fat: Float,
    @ColumnInfo("dietary_fiber")
    val dietaryFiber: Float,
    val sugar: Float,
    val sodium: Float
)
