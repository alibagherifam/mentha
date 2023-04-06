package dev.alibagherifam.mentha.camera

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.alibagherifam.mentha.R
import dev.alibagherifam.mentha.comoon.FoodImage
import dev.alibagherifam.mentha.comoon.LocalizationPreviews
import dev.alibagherifam.mentha.comoon.getSampleFood
import dev.alibagherifam.mentha.comoon.provideFormatEnergyUseCase
import dev.alibagherifam.mentha.nutritionfacts.model.FoodEntity
import dev.alibagherifam.mentha.theme.AppTheme

@Composable
fun AnimatedRecognitionCard(
    food: FoodEntity?,
    onShowDetailsClick: () -> Unit,
    modifier: Modifier = Modifier
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
        enter = fadeIn(
            tween(durationMillis = 500)
        ) + slideInVertically(
            tween(durationMillis = 500),
            initialOffsetY = { it / 2 }
        ),
        exit = fadeOut(
            tween(durationMillis = 500, delayMillis = 1000)
        ) + slideOutVertically(
            tween(durationMillis = 500, delayMillis = 1000),
            targetOffsetY = { it / 2 }
        )
    ) {
        safeFood.value?.let { RecognitionCard(food = it, onShowDetailsClick) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecognitionCard(
    food: FoodEntity,
    onShowDetailsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(onClick = onShowDetailsClick, modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .padding(start = 12.dp)
        ) {
            val formatEnergy = provideFormatEnergyUseCase(LocalContext.current)
            FoodImage(
                imageUri = food.image,
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.size(14.dp))
            Column(Modifier) {
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
                        text = formatEnergy(food.nutritionFacts.energy),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
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

@LocalizationPreviews
@Composable
fun RecognitionCardPreview() {
    AppTheme {
        RecognitionCard(
            food = getSampleFood(),
            onShowDetailsClick = {}
        )
    }
}
