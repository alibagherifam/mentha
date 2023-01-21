package com.alibagherifam.mentha.details

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alibagherifam.mentha.R
import com.alibagherifam.mentha.camera.stringFormatted
import com.alibagherifam.mentha.nutritionfacts.getSampleFood
import com.alibagherifam.mentha.nutritionfacts.model.FoodEntity
import com.alibagherifam.mentha.theme.AppTheme
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailsScreen(
    food: FoodEntity,
    onShareClick: () -> Unit,
    onBackPressed: () -> Unit
) {
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            TopBar(
                title = food.name,
                onBackPressed = onBackPressed,
                onShareClick = onShareClick
            )
        }
    ) { innerPadding ->
        Column(
            Modifier
                .verticalScroll(scrollState)
                .padding(innerPadding)
                .padding(all = 16.dp)
        ) {
            FoodSummary(
                iconRes = food.icon,
                name = food.name,
                summary = food.summary
            )
            Spacer(modifier = Modifier.size(16.dp))
            NutritionFacts(food)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    onBackPressed: () -> Unit,
    onShareClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(id = R.drawable.ic_chevron_left),
                    contentDescription = stringResource(R.string.content_description_back_button)
                )
            }
        },
        actions = {
            IconButton(onClick = onShareClick) {
                Icon(
                    modifier = Modifier
                        .size(28.dp)
                        .padding(all = 3.dp),
                    painter = painterResource(id = R.drawable.ic_share),
                    contentDescription = stringResource(R.string.content_description_share_button)
                )
            }
        }
    )
}

@Composable
fun FoodSummary(
    @DrawableRes iconRes: Int,
    name: String,
    summary: String
) {
    Column {
        FoodIcon(iconRes)
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = name,
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = summary,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun FoodIcon(@DrawableRes iconRes: Int) {
    Image(
        modifier = Modifier
            .padding(start = 18.dp)
            .size(160.dp)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = CircleShape
            ),
        painter = painterResource(id = iconRes),
        contentDescription = "food icon"
    )
}

@Composable
fun NutritionFacts(food: FoodEntity) {
    Column(
        Modifier
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        NutritionFactsHeader(food)
        Spacer(modifier = Modifier.size(10.dp))
        Divider(thickness = 2.dp)
        Spacer(modifier = Modifier.size(12.dp))
        NutritionFactsContent(food)
    }
}

@Composable
fun NutritionFactsHeader(food: FoodEntity) {
    Text(
        style = MaterialTheme.typography.headlineMedium,
        text = stringResource(id = R.string.label_nutrition_facts)
    )
    Text(
        style = MaterialTheme.typography.titleLarge,
        text = stringResource(
            R.string.label_serving_size,
            food.nutritionFact.measure,
            food.name,
            food.nutritionFact.weight
        )
    )
}

@Composable
fun NutritionFactsContent(food: FoodEntity) {
    listOf(
        "آب" to food.nutritionFact.water,
        "پروتئین" to food.nutritionFact.protein,
        "کربوهیدرات" to food.nutritionFact.carbohydrate,
        "چربی" to food.nutritionFact.fat,
        "قند" to food.nutritionFact.sugar
    ).forEach {
        Nutrition(
            nutritionName = it.first,
            nutritionWeight = it.second,
            foodWeight = food.nutritionFact.weight
        )
        Spacer(modifier = Modifier.size(16.dp))
    }
}

@Composable
fun Nutrition(
    nutritionName: String,
    nutritionWeight: Float,
    foodWeight: Int
) {
    val factor = nutritionWeight / foodWeight
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
                style = MaterialTheme.typography.titleSmall,
                text = stringResource(
                    id = R.string.label_weight_in_gram,
                    nutritionWeight.stringFormatted()
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = nutritionPercentage,
                modifier = Modifier.widthIn(min = 28.dp),
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )
        }
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), progress = factor)
    }
}

@Preview
@Composable
fun FoodDetailsScreenPreview() {
    AppTheme {
        FoodDetailsScreen(
            getSampleFood(),
            onBackPressed = {},
            onShareClick = {}
        )
    }
}
