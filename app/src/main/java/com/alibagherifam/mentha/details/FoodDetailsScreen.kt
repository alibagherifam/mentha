package com.alibagherifam.mentha.details

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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.alibagherifam.mentha.R
import com.alibagherifam.mentha.camera.stringFormatted
import com.alibagherifam.mentha.nutritionfacts.model.FoodEntity
import com.alibagherifam.mentha.nutritionfacts.model.NutritionFacts
import com.alibagherifam.mentha.sampledata.LocalizationPreview
import com.alibagherifam.mentha.sampledata.getSampleFood
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
                name = food.name,
                summary = food.summary,
                imageUri = food.image
            )
            Spacer(modifier = Modifier.size(16.dp))
            NutritionFacts(food.nutritionFacts)
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
                    painter = painterResource(id = R.drawable.ic_chevron_toward_start),
                    contentDescription = stringResource(R.string.a11y_back_button)
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
                    contentDescription = stringResource(R.string.a11y_share_button)
                )
            }
        }
    )
}

@Composable
fun FoodSummary(
    name: String,
    summary: String,
    imageUri: String
) {
    Column {
        FoodImage(imageUri)
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
fun FoodImage(imageUri: String) {
    AsyncImage(
        modifier = Modifier
            .padding(start = 20.dp)
            .size(150.dp)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = CircleShape
            ),
        model = imageUri,
        placeholder = painterResource(R.drawable.img_food_placeholder),
        contentDescription = stringResource(R.string.a11y_food_image)
    )
}

@Composable
fun NutritionFacts(data: NutritionFacts) {
    Column(
        Modifier
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        NutritionFactsHeader(data)
        Spacer(modifier = Modifier.size(10.dp))
        Divider(thickness = 2.dp)
        Spacer(modifier = Modifier.size(12.dp))
        NutritionFactsContent(data)
    }
}

@Composable
fun NutritionFactsHeader(data: NutritionFacts) {
    Text(
        style = MaterialTheme.typography.headlineMedium,
        text = stringResource(R.string.label_nutrition_facts)
    )
    Text(
        style = MaterialTheme.typography.titleLarge,
        text = stringResource(
            R.string.label_serving_size,
            data.servingSize,
            data.servingWeight
        )
    )
}

@Composable
fun NutritionFactsContent(data: NutritionFacts) {
    listOf(
        R.string.label_water to data.water,
        R.string.label_protein to data.protein,
        R.string.label_carbohydrate to data.carbohydrate,
        R.string.label_fat to data.fat,
        R.string.label_sugar to data.sugar
    ).forEach {
        MicroNutrition(
            name = stringResource(it.first),
            weight = it.second,
            totalWeight = data.servingWeight
        )
        Spacer(modifier = Modifier.size(16.dp))
    }
}

@Composable
fun MicroNutrition(
    name: String,
    weight: Float,
    totalWeight: Int
) {
    val factor = weight / totalWeight
    val nutritionPercentage = (factor * 100).roundToInt().let {
        if (it < 1) "<1%" else "$it%"
    }
    Column {
        Row {
            Text(
                style = MaterialTheme.typography.titleMedium,
                text = name
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                style = MaterialTheme.typography.titleSmall,
                text = stringResource(
                    id = R.string.label_weight_in_gram,
                    weight.stringFormatted()
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

@LocalizationPreview
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
