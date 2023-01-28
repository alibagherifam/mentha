package com.alibagherifam.mentha.camera

import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alibagherifam.mentha.R
import com.alibagherifam.mentha.sampledata.LocalizationPreview
import com.alibagherifam.mentha.sampledata.getSampleFood
import com.alibagherifam.mentha.theme.AppTheme

@Composable
fun CameraScreen(
    uiState: CameraUiState,
    onFlashlightToggle: (Boolean) -> Unit,
    onSettingsClick: () -> Unit,
    onShowDetailsClick: () -> Unit,
    onPreviewViewCreated: ((PreviewView) -> Unit)?
) {
    Box(Modifier.fillMaxSize()) {
        CameraPreviewView(onPreviewViewCreated)
        ActionBar(
            Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            uiState.isFlashlightSupported,
            uiState.isFlashlightEnabled,
            onFlashlightToggle,
            onSettingsClick
        )
        ScanAreaRectangle(Modifier.align(Alignment.Center))
        AnimatedRecognitionCard(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp)
                .padding(bottom = 20.dp),
            uiState.food,
            onShowDetailsClick
        )
    }
}

@Composable
fun ActionBar(
    modifier: Modifier = Modifier,
    isFlashlightSupported: Boolean,
    isFlashlightEnabled: Boolean,
    onFlashlightToggle: (Boolean) -> Unit,
    onSettingsClick: () -> Unit
) {
    Row(modifier) {
        FilledTonalIconButton(onClick = onSettingsClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_settings),
                contentDescription = stringResource(R.string.a11y_settings_button)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (isFlashlightSupported) {
            FilledTonalIconToggleButton(
                checked = isFlashlightEnabled,
                onCheckedChange = onFlashlightToggle
            ) {
                val icon =
                    if (isFlashlightEnabled) R.drawable.ic_flash_on else R.drawable.ic_flash_off
                Icon(
                    painter = painterResource(icon),
                    contentDescription = stringResource(R.string.a11y_flashlight_toggle)
                )
            }
        }
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

@LocalizationPreview
@Composable
fun CameraScreenPreview() {
    AppTheme {
        CameraScreen(
            uiState = CameraUiState(food = getSampleFood()),
            onFlashlightToggle = {},
            onSettingsClick = {},
            onShowDetailsClick = {},
            onPreviewViewCreated = null
        )
    }
}
