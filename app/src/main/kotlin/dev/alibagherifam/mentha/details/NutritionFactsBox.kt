package dev.alibagherifam.mentha.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.alibagherifam.mentha.R
import dev.alibagherifam.mentha.comoon.FormatNutritionWeightUseCase
import dev.alibagherifam.mentha.comoon.LocalizationPreviews
import dev.alibagherifam.mentha.comoon.getSampleFood
import dev.alibagherifam.mentha.comoon.provideFormatEnergyUseCase
import dev.alibagherifam.mentha.comoon.provideFormatNutritionWeightUseCase
import dev.alibagherifam.mentha.nutritionfacts.model.NutritionFacts
import dev.alibagherifam.mentha.theme.MenthaTheme
import kotlin.math.roundToInt

@Composable
fun NutritionFactsBox(
    data: NutritionFacts,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier,
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Column(Modifier.padding(horizontal = 24.dp, vertical = 12.dp)) {
            ServingSize(data)
            Spacer(Modifier.size(10.dp))
            HorizontalDivider(thickness = 2.dp)
            Spacer(Modifier.size(12.dp))
            NutritionList(data)
        }
    }
}

@Composable
fun ServingSize(
    data: NutritionFacts,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = stringResource(R.string.label_nutrition_facts),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = stringResource(
                R.string.label_serving_size,
                data.servingSize,
                stringResource(
                    R.string.label_weight_in_gram,
                    data.servingWeight.toString()
                )
            ),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun NutritionList(
    data: NutritionFacts,
    modifier: Modifier = Modifier
) {
    val formatEnergy = provideFormatEnergyUseCase(LocalContext.current)
    val formatNutritionWeight = provideFormatNutritionWeightUseCase(LocalContext.current)
    val calorieLabel = stringResource(R.string.label_energy)
    val calorieValue = formatEnergy(data.energy)
    Column(modifier) {
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
                formatNutritionWeight = formatNutritionWeight,
                nutritionName = stringResource(it.first),
                nutritionWeight = it.second,
                foodServingWeight = data.servingWeight
            )
            Spacer(Modifier.size(16.dp))
        }
    }
}

@Composable
fun Nutrition(
    formatNutritionWeight: FormatNutritionWeightUseCase,
    nutritionName: String,
    nutritionWeight: Float,
    foodServingWeight: Int,
    modifier: Modifier = Modifier
) {
    val factor = nutritionWeight / foodServingWeight
    val nutritionPercentage = (factor * 100).roundToInt().let {
        if (it < 1) "<1%" else "$it%"
    }
    Column(modifier) {
        Row {
            Text(
                text = nutritionName,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.size(8.dp))
            Text(
                text = formatNutritionWeight(nutritionWeight),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = nutritionPercentage,
                modifier = Modifier.widthIn(min = 28.dp),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
        LinearProgressIndicator(
            progress = { factor },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@LocalizationPreviews
@Composable
private fun NutritionFactsPreview() {
    MenthaTheme {
        NutritionFactsBox(getSampleFood().nutritionFacts)
    }
}
