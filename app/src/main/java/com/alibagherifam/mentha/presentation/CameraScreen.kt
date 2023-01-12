package com.alibagherifam.mentha.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.alibagherifam.mentha.R
import com.alibagherifam.mentha.business.ImageAnalyzer
import com.alibagherifam.mentha.nutritionfacts.Food
import com.alibagherifam.mentha.nutritionfacts.getSampleFood
import com.alibagherifam.mentha.theme.AppTheme

@Composable
fun CameraScreen(
    food: Food?,
    isFlashlightOn: Boolean,
    onFlashlightToggle: (Boolean) -> Unit,
    onSettingsClick: () -> Unit,
    onShowDetailsClick: () -> Unit,
    imageAnalyzer: ImageAnalyzer?
) {
    Box(Modifier.fillMaxSize()) {
        CameraPreviewView(imageAnalyzer)
        ActionBar(
            Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            isFlashlightOn,
            onFlashlightToggle,
            onSettingsClick
        )
        ScanAreaRectangle(Modifier.align(Alignment.Center))
        if (food != null) {
            RecognitionCard(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 20.dp),
                food,
                onShowDetailsClick
            )
        }
    }
}

@Composable
fun ActionBar(
    modifier: Modifier = Modifier,
    isFlashlightEnabled: Boolean,
    onFlashlightToggle: (Boolean) -> Unit,
    onSettingsClick: () -> Unit
) {
    Row(modifier) {
        FlashlightToggleButton(isFlashlightEnabled, onFlashlightToggle)
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onSettingsClick) {
            Icon(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colors.surface,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(8.dp)
                    .size(32.dp),
                painter = painterResource(id = R.drawable.ic_settings),
                contentDescription = stringResource(R.string.content_description_settings_button)
            )
        }
    }
}

@Composable
fun FlashlightToggleButton(
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    IconToggleButton(
        checked = isEnabled,
        onCheckedChange = onToggle
    ) {
        val bgColor = with(MaterialTheme.colors) { if (isEnabled) secondary else surface }
        Icon(
            modifier = Modifier
                .background(color = bgColor, shape = MaterialTheme.shapes.medium)
                .padding(8.dp)
                .size(32.dp),
            painter = painterResource(
                if (isEnabled) R.drawable.ic_flash_on else R.drawable.ic_flash_off
            ),
            tint = contentColorFor(backgroundColor = bgColor),
            contentDescription = stringResource(R.string.content_description_flashlight_button)
        )
    }
}

@Composable
fun ScanAreaRectangle(modifier: Modifier) {
    Image(
        contentScale = ContentScale.FillBounds,
        modifier = modifier
            .fillMaxWidth(fraction = 0.80f)
            .fillMaxHeight(0.56f)
            .offset(y = (-50).dp),
        painter = painterResource(id = R.drawable.bg_scan_square),
        contentDescription = ""
    )
}

@Preview
@Composable
fun CameraScreenPreview() {
    AppTheme {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            CameraScreen(
                getSampleFood(),
                isFlashlightOn = true,
                {}, {}, {},
                imageAnalyzer = null
            )
        }
    }
}
