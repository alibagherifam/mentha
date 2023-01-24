package com.alibagherifam.mentha.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Divider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alibagherifam.mentha.R
import com.alibagherifam.mentha.camera.stringFormatted
import com.alibagherifam.mentha.nutritionfacts.model.NutritionFacts
import com.alibagherifam.mentha.sampledata.LocalizationPreview
import com.alibagherifam.mentha.sampledata.getSampleFood
import com.alibagherifam.mentha.theme.AppTheme
import kotlin.math.roundToInt

@Composable
fun NutritionFactsBox(data: NutritionFacts) {
    OutlinedCard(
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Column(Modifier.padding(horizontal = 24.dp, vertical = 12.dp)) {
            ServingSize(data)
            Spacer(modifier = Modifier.size(10.dp))
            Divider(thickness = 2.dp)
            Spacer(modifier = Modifier.size(12.dp))
            NutritionList(data)
        }
    }
}

@Composable
fun ServingSize(data: NutritionFacts) {
    Text(
        style = MaterialTheme.typography.headlineMedium,
        text = stringResource(R.string.label_nutrition_facts)
    )
    Text(
        style = MaterialTheme.typography.titleLarge,
        text = stringResource(
            R.string.label_serving_size,
            data.servingSize,
            stringResource(
                R.string.label_weight_in_gram,
                data.servingWeight.toString()
            )
        )
    )
}

@Composable
fun NutritionList(data: NutritionFacts) {
    val calorieLabel = stringResource(R.string.label_energy)
    val calorieValue = stringResource(
        R.string.label_energy_in_kilo_calorie,
        data.energy.stringFormatted()
    )
    Text(
        text = "$calorieLabel: $calorieValue",
        style = MaterialTheme.typography.titleLarge
    )
    Spacer(modifier = Modifier.size(18.dp))
    listOf(
        R.string.label_water to data.water,
        R.string.label_protein to data.protein,
        R.string.label_carbohydrate to data.carbohydrate,
        R.string.label_fat to data.fat,
        R.string.label_sugar to data.sugar
    ).forEach {
        Nutrition(
            nutritionName = stringResource(it.first),
            nutritionWeight = it.second,
            foodServingWeight = data.servingWeight
        )
        Spacer(modifier = Modifier.size(16.dp))
    }
}

@Composable
fun Nutrition(
    nutritionName: String,
    nutritionWeight: Float,
    foodServingWeight: Int
) {
    val factor = nutritionWeight / foodServingWeight
    val nutritionPercentage = (factor * 100).roundToInt().let {
        if (it < 1) "<1%" else "$it%"
    }
    Column {
        Row {
            Text(
                style = MaterialTheme.typography.titleMedium,
                text = nutritionName
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                style = MaterialTheme.typography.titleMedium,
                text = stringResource(
                    id = R.string.label_weight_in_gram,
                    nutritionWeight.stringFormatted()
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = nutritionPercentage,
                modifier = Modifier.widthIn(min = 28.dp),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), progress = factor)
    }
}

@LocalizationPreview
@Composable
fun NutritionFactsPreview() {
    AppTheme {
        NutritionFactsBox(getSampleFood().nutritionFacts)
    }
}
