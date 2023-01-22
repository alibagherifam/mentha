package com.alibagherifam.mentha.camera

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.alibagherifam.mentha.R
import com.alibagherifam.mentha.nutritionfacts.model.FoodEntity
import com.alibagherifam.mentha.sampledata.LocalizationPreview
import com.alibagherifam.mentha.sampledata.getSampleFood
import com.alibagherifam.mentha.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecognitionCard(
    modifier: Modifier = Modifier,
    food: FoodEntity,
    onShowDetailsClick: () -> Unit
) {
    Card(modifier = modifier, onClick = onShowDetailsClick) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            RecognitionInfo(food)
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = onShowDetailsClick) {
                Text(text = stringResource(R.string.label_nutrition_facts))
                Icon(
                    painter = painterResource(R.drawable.ic_chevron_toward_end),
                    contentDescription = ""
                )
            }
        }
    }
}

@Composable
fun RecognitionInfo(food: FoodEntity) {
    AsyncImage(
        modifier = Modifier.size(60.dp),
        model = food.image,
        contentDescription = stringResource(R.string.a11y_food_image),
        placeholder = painterResource(R.drawable.img_food_placeholder)
    )
    Spacer(modifier = Modifier.size(10.dp))
    Column {
        Text(
            text = food.name,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.size(10.dp))
        // TODO: Replace fire image with vector one
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.primary,
                painter = painterResource(R.drawable.img_fire),
                contentDescription = "Calorie Icon"
            )
            Spacer(modifier = Modifier.size(6.dp))
            Text(
                text = stringResource(
                    R.string.label_energy_in_kilo_calorie,
                    food.nutritionFacts.energy
                ),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@LocalizationPreview
@Composable
fun RecognitionCardPreview() {
    AppTheme {
        RecognitionCard(food = getSampleFood()) {}
    }
}
