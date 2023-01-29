package com.alibagherifam.mentha.camera

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.node.Ref
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alibagherifam.mentha.R
import com.alibagherifam.mentha.comoon.FoodImage
import com.alibagherifam.mentha.comoon.stringFormatted
import com.alibagherifam.mentha.nutritionfacts.model.FoodEntity
import com.alibagherifam.mentha.comoon.LocalizationPreview
import com.alibagherifam.mentha.comoon.getSampleFood
import com.alibagherifam.mentha.theme.AppTheme

@Composable
fun AnimatedRecognitionCard(
    modifier: Modifier,
    food: FoodEntity?,
    onShowDetailsClick: () -> Unit
) {
    val safeFood = remember {
        Ref<FoodEntity>()
    }

    food?.let {
        safeFood.value = it
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = food != null,
        enter = fadeIn(tween(durationMillis = 500)) +
                slideInVertically(tween(durationMillis = 500)) { it / 2 },
        exit = fadeOut(tween(durationMillis = 500, delayMillis = 1000)) +
                slideOutVertically(tween(durationMillis = 500, delayMillis = 1000)) { it / 2 }
    ) {
        safeFood.value?.let { RecognitionCard(food = it, onShowDetailsClick) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecognitionCard(
    food: FoodEntity,
    onShowDetailsClick: () -> Unit
) {
    Card(onClick = onShowDetailsClick) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .padding(start = 12.dp)
        ) {
            RecognitionInfo(food)
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = onShowDetailsClick) {
                Text(text = stringResource(R.string.label_nutrition_facts))
                Icon(
                    painter = painterResource(R.drawable.ic_chevron_toward_end),
                    contentDescription = stringResource(R.string.a11y_show_nutrition_facts)
                )
            }
        }
    }
}

@Composable
fun RecognitionInfo(food: FoodEntity) {
    FoodImage(
        modifier = Modifier.size(60.dp),
        imageUri = food.image,
    )
    Spacer(modifier = Modifier.size(14.dp))
    Column {
        Text(
            text = food.name,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.size(6.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.primary,
                painter = painterResource(R.drawable.img_fire),
                contentDescription = stringResource(R.string.a11y_energy_icon)
            )
            Spacer(modifier = Modifier.size(6.dp))
            Text(
                text = stringResource(
                    R.string.label_energy_in_kilo_calorie,
                    food.nutritionFacts.energy.stringFormatted()
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
