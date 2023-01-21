package com.alibagherifam.mentha.details

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alibagherifam.mentha.R
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
                        onShareClick = { shareFoodDetails() },
                        onBackPressed = { finish() }
                    )
                }
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

    private suspend fun getFoodFromIntent(intent: Intent): FoodEntity {
        val foodId = intent.extras?.getString(FoodEntity::id.name)!!
        return repository.getFood(foodId)
    }
}
