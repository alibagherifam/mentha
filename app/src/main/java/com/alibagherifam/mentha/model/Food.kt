package com.alibagherifam.mentha.model

import androidx.annotation.DrawableRes
import kotlinx.serialization.Serializable

@Serializable
data class Food(
    val id: String,
    val name: String,
    val summary: String,
    @DrawableRes val icon: Int? = null,
    val weight: Int,
    val measure: String,
    val energy: Float,
    val water: Float,
    val protein: Float,
    val carbohydrate: Float,
    val fat: Float,
    val dietaryFiber: Float,
    val sugar: Float,
    val sodium: Float
)
