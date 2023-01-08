package com.alibagherifam.mentha.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alibagherifam.mentha.databinding.ListItemNutritionBinding
import com.alibagherifam.mentha.model.Food

class NutritionFactsAdapter(
    private val food: Food
) : RecyclerView.Adapter<NutritionViewHolder>() {

    private val data = listOf(
        "آب" to food.water,
        "پروتئین" to food.protein,
        "کربوهیدرات" to food.carbohydrate,
        "چربی" to food.fat,
        "قند" to food.sugar
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NutritionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemNutritionBinding.inflate(inflater, parent, false)
        return NutritionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NutritionViewHolder, position: Int) {
        val nutrition = data[position]
        holder.bind(nutrition.first, nutrition.second, food.weight)
    }

    override fun getItemCount() = data.size
}
