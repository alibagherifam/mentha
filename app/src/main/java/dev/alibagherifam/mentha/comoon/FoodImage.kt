package dev.alibagherifam.mentha.comoon

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import dev.alibagherifam.mentha.R

@Composable
fun FoodImage(imageUri: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = imageUri,
        contentDescription = stringResource(R.string.a11y_food_image),
        modifier = modifier,
        placeholder = painterResource(R.drawable.img_food_placeholder),
        error = painterResource(R.drawable.img_food_placeholder)
    )
}
