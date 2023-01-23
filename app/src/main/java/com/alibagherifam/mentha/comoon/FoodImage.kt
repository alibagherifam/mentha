package com.alibagherifam.mentha.comoon

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.alibagherifam.mentha.R

@Composable
fun FoodImage(modifier: Modifier = Modifier, imageUri: String) {
    AsyncImage(
        modifier = modifier,
        model = imageUri,
        contentDescription = stringResource(R.string.a11y_food_image),
        placeholder = painterResource(R.drawable.img_food_placeholder),
        error = painterResource(R.drawable.img_food_placeholder)
    )
}
