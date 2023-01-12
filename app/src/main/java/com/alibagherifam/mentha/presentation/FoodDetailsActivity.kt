package com.alibagherifam.mentha.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.alibagherifam.mentha.R
import com.alibagherifam.mentha.nutritionfacts.Food
import com.alibagherifam.mentha.nutritionfacts.FoodRepository
import com.alibagherifam.mentha.theme.AppTheme

class FoodDetailsActivity : AppCompatActivity() {

    private lateinit var food: Food

    private val repository: FoodRepository by lazy {
        FoodRepository.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        food = getFoodFromIntent(intent)
        setContent {
            AppTheme {
                FoodDetailsScreen(
                    food = food,
                    onShareClick = { shareFoodDetails() },
                    onBackPressed = { finish() }
                )
            }
        }
    }

    private fun shareFoodDetails() {
        val appInfo = getString(R.string.message_share_text)
        val content = """
            "${food.summary}"
            
            $appInfo
        """.trimIndent()

        val chooserIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, content)
            type = "text/plain"
        }

        val chooserTitle = getString(R.string.label_share)
        val i = Intent.createChooser(chooserIntent, chooserTitle).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(i)
    }

    private fun getFoodFromIntent(intent: Intent): Food {
        val foodId = intent.extras?.getString(Food::id.name)!!
        return repository.getFood(foodId)
    }
}
