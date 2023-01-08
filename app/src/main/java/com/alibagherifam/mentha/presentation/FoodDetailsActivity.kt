package com.alibagherifam.mentha.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.alibagherifam.mentha.R
import com.alibagherifam.mentha.databinding.ActivityFoodDetailsBinding
import com.alibagherifam.mentha.model.Food
import com.alibagherifam.mentha.model.FoodRepository

class FoodDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodDetailsBinding
    private lateinit var food: Food

    private val repository: FoodRepository by lazy {
        FoodRepository.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        food = getFoodFromIntent(intent)
        setupAppBar(title = food.name)
        updateFoodDetails(food)
        binding.listNutritionFacts.adapter = NutritionFactsAdapter(food)
    }

    private fun setupAppBar(title: String) {
        val appBar = binding.appBar
        appBar.title = title
        appBar.setNavigationOnClickListener {
            finish()
        }
        appBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btn_share -> {
                    shareFoodDetails()
                    true
                }
                else -> false
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

    private fun updateFoodDetails(food: Food) {
        binding.apply {
            tvServingSize.text = getString(
                R.string.label_serving_size,
                food.measure,
                food.name,
                food.weight
            )
            tvFoodName.text = food.name
            tvFoodSummary.text = food.summary
            imgFoodIcon.load(food.icon ?: R.drawable.img_banana)
        }
    }
}
