package com.alibagherifam.mentha.presentation

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alibagherifam.mentha.R
import com.alibagherifam.mentha.nutritionfacts.Food
import com.alibagherifam.mentha.nutritionfacts.getSampleFood
import com.alibagherifam.mentha.theme.AppTheme
import com.alibagherifam.mentha.utils.stringFormatted
import kotlin.math.roundToInt

@Composable
fun FoodDetailsScreen(
    food: Food,
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
        },
        content = { innerPadding ->
            Column(
                Modifier
                    .verticalScroll(scrollState)
                    .padding(innerPadding)
                    .padding(all = 16.dp)
            ) {
                FoodSummary(
                    iconRes = food.icon!!,
                    name = food.name,
                    summary = food.summary
                )
                Spacer(modifier = Modifier.size(16.dp))
                NutritionFacts(food)
            }
        }
    )
}

@Composable
fun TopBar(
    title: String,
    onBackPressed: () -> Unit,
    onShareClick: () -> Unit
) {
    TopAppBar(
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
            style = MaterialTheme.typography.h3
        )
        Spacer(modifier = Modifier.size(8.dp))
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = summary,
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
fun FoodIcon(@DrawableRes iconRes: Int) {
    Image(
        modifier = Modifier
            .padding(start = 18.dp)
            .size(160.dp)
            .background(
                color = MaterialTheme.colors.secondary,
                shape = CircleShape
            ),
        painter = painterResource(id = iconRes),
        contentDescription = "food icon"
    )
}

@Composable
fun NutritionFacts(food: Food) {
    Column(
        Modifier
            .border(
                width = 2.dp,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
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
fun NutritionFactsHeader(food: Food) {
    Text(
        style = MaterialTheme.typography.h4,
        text = stringResource(id = R.string.label_nutrition_facts)
    )
    Text(
        style = MaterialTheme.typography.h6,
        text = stringResource(
            R.string.label_serving_size,
            food.measure,
            food.name,
            food.weight
        )
    )
}

@Composable
fun NutritionFactsContent(food: Food) {
    listOf(
        "آب" to food.water,
        "پروتئین" to food.protein,
        "کربوهیدرات" to food.carbohydrate,
        "چربی" to food.fat,
        "قند" to food.sugar
    ).forEach {
        Nutrition(
            nutritionName = it.first,
            nutritionWeight = it.second,
            foodWeight = food.weight
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
                style = MaterialTheme.typography.subtitle1,
                text = nutritionName
            )
            Spacer(modifier = Modifier.size(8.dp))
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    style = MaterialTheme.typography.subtitle2,
                    text = stringResource(
                        id = R.string.label_weight_in_gram,
                        nutritionWeight.stringFormatted()
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    style = MaterialTheme.typography.subtitle2,
                    text = nutritionPercentage
                )
            }
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
