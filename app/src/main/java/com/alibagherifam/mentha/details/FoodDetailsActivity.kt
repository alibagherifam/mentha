package com.alibagherifam.mentha.details

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alibagherifam.mentha.R
import com.alibagherifam.mentha.comoon.stringFormatted
import com.alibagherifam.mentha.nutritionfacts.model.FoodEntity
import com.alibagherifam.mentha.nutritionfacts.FoodRepository
import com.alibagherifam.mentha.nutritionfacts.provideFoodRepository
import com.alibagherifam.mentha.theme.AppTheme
import kotlinx.coroutines.launch

class FoodDetailsActivity : AppCompatActivity() {

    private lateinit var food: FoodEntity

    private val repository: FoodRepository by lazy {
        provideFoodRepository(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            food = getFoodFromIntent(intent)
            setContent {
                AppTheme {
                    FoodDetailsScreen(
                        food = food,
                        onShareClick = { shareNutritionFacts() },
                        onBackPressed = { finish() }
                    )
                }
            }
        }
    }

    private fun shareNutritionFacts() {
        val chooserIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, prepareShareNutritionFactsMessage())
            type = "text/plain"
        }

        val chooserTitle = getString(R.string.label_share)
        val i = Intent.createChooser(chooserIntent, chooserTitle).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(i)
    }

    private fun prepareShareNutritionFactsMessage(): String {
        val foodEnergy = getString(
            R.string.label_energy_in_kilo_calorie,
            food.nutritionFacts.energy.stringFormatted()
        )
        val foodCarbohydrate = getString(
            R.string.label_weight_in_gram,
            food.nutritionFacts.carbohydrate.stringFormatted()
        )
        return """
            "${food.name}"
            ${getString(R.string.label_energy)}: $foodEnergy
            ${getString(R.string.label_carbohydrate)}: $foodCarbohydrate
            
            ${getDownloadAppMessage()}
        """.trimIndent()
    }

    private fun getDownloadAppMessage(): String {
        val downloadAppUrl = getString(R.string.download_app_url)
        return getString(R.string.message_download_app, downloadAppUrl)
    }

    private suspend fun getFoodFromIntent(intent: Intent): FoodEntity {
        val foodId = intent.extras?.getString(FoodEntity::id.name)!!
        return repository.getFood(foodId)
    }
}
