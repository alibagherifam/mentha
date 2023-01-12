package com.alibagherifam.mentha.camera

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alibagherifam.mentha.R
import com.alibagherifam.mentha.nutritionfacts.Food

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecognitionCard(
    modifier: Modifier,
    food: Food,
    onShowDetailsClick: () -> Unit
) {
    Card(modifier = modifier, onClick = onShowDetailsClick) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Image(
                modifier = Modifier.size(60.dp),
                painter = painterResource(food.icon!!),
                contentDescription = stringResource(R.string.content_description_food_image)
            )
            Spacer(modifier = Modifier.size(10.dp))
            Column {
                Text(style = MaterialTheme.typography.h5, text = food.name)
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    style = MaterialTheme.typography.subtitle1,
                    text = stringResource(R.string.label_energy_in_kilo_calorie, food.energy)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = onShowDetailsClick) {
                Text(text = stringResource(R.string.label_nutrition_facts))
                Icon(painter = painterResource(R.drawable.ic_chevron_left), contentDescription = "")
            }
        }
    }
}
