package com.alibagherifam.mentha.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alibagherifam.mentha.R
import com.alibagherifam.mentha.comoon.FoodImage
import com.alibagherifam.mentha.nutritionfacts.model.FoodEntity
import com.alibagherifam.mentha.sampledata.LocalizationPreview
import com.alibagherifam.mentha.sampledata.getSampleFood
import com.alibagherifam.mentha.theme.AppTheme

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
            FoodDetailsTopBar(
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
            FoodDetails(
                name = food.name,
                summary = food.summary,
                imageUri = food.image
            )
            Spacer(modifier = Modifier.size(16.dp))
            NutritionFactsBox(food.nutritionFacts)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailsTopBar(
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
fun FoodDetails(
    name: String,
    summary: String,
    imageUri: String
) {
    Column {
        FoodImage(
            modifier = Modifier
                .padding(start = 20.dp)
                .size(150.dp)
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = CircleShape
                ),
            imageUri = imageUri
        )
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
