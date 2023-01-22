package com.alibagherifam.mentha.nutritionfacts.model

import androidx.room.ColumnInfo

/**
 * Value object (composite attribute) for micro-nutrition
 * @param weight: weight of 1 unit of measure in gram
 * @param measure: how the food is measured (cup, tbsp, etc.)
 * @param energy: energy in kilo calorie
 */
data class NutritionFacts(
    val weight: Int,
    val measure: String,
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
