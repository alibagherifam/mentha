package dev.alibagherifam.mentha.details

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.alibagherifam.mentha.R
import dev.alibagherifam.mentha.comoon.provideFoodDetailsViewModelFactory
import dev.alibagherifam.mentha.nutritionfacts.model.FoodEntity
import dev.alibagherifam.mentha.theme.MenthaTheme

class FoodDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MenthaTheme {
                FoodDetailsFragment(
                    viewModel = viewModel(
                        factory = provideFoodDetailsViewModelFactory(
                            context = this,
                            foodId = intent.extras?.getString(FoodEntity::id.name)!!
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
