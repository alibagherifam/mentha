package com.alibagherifam.mentha.details

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alibagherifam.mentha.R
import com.alibagherifam.mentha.comoon.provideFoodDetailsViewModelFactory
import com.alibagherifam.mentha.nutritionfacts.model.FoodEntity
import com.alibagherifam.mentha.theme.AppTheme

class FoodDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                FoodDetailsFragment(
                    viewModel = viewModel(
                        factory = provideFoodDetailsViewModelFactory(
                            foodId = intent.extras?.getString(FoodEntity::id.name)!!,
                            context = this
                        )
                    )
                )
            }
        }
    }

    @Composable
    private fun FoodDetailsFragment(
        viewModel: FoodDetailsViewModel
    ) {
        val food by viewModel.uiState.collectAsState()
        food?.let {
            FoodDetailsScreen(
                food = it,
                onShareClick = {
                    shareMessage(viewModel.prepareShareNutritionFactsMessage())
                },
                onBackPressed = { finish() }
            )
        }
    }

    private fun shareMessage(message: String) {
        val chooserIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }

        val chooserTitle = getString(R.string.label_share)
        val i = Intent.createChooser(chooserIntent, chooserTitle).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(i)
    }
}
