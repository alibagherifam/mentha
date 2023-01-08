package com.alibagherifam.mentha.presentation

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.alibagherifam.mentha.R
import com.alibagherifam.mentha.databinding.ListItemNutritionBinding
import com.alibagherifam.mentha.utils.stringFormatted
import kotlin.math.roundToInt

class NutritionViewHolder(
    private val binding: ListItemNutritionBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val context: Context get() = itemView.context

    fun bind(
        nutritionName: String,
        nutritionWeight: Float,
        foodWeight: Int
    ) {
        binding.apply {
            tvName.text = nutritionName
            tvWeight.text = context.getString(
                R.string.label_weight_in_gram,
                nutritionWeight.stringFormatted()
            )

            val nutritionPercentage = calculatePercentage(
                nutritionWeight,
                foodWeight
            )
            progressPercentage.progress = nutritionPercentage
            tvPercentage.text = formatPercentage(nutritionPercentage)
        }
    }

    private fun calculatePercentage(
        nutritionWeight: Float,
        foodWeight: Int
    ): Int = (nutritionWeight * 100 / foodWeight).roundToInt()

    private fun formatPercentage(nutritionPercentage: Int): String =
        if (nutritionPercentage < 1) {
            context.getString(R.string.label_less_than_one_percent)
        } else {
            context.getString(R.string.label_percentage, nutritionPercentage)
        }
}
