package dev.alibagherifam.mentha.permission

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.alibagherifam.mentha.R
import dev.alibagherifam.mentha.comoon.LocalizationPreview
import dev.alibagherifam.mentha.theme.AppTheme

@Composable
fun CameraPermissionRationaleDialog(
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = stringResource(R.string.label_camera_permission))
        },
        text = {
            Text(text = stringResource(R.string.message_camera_permission_required))
        },
        confirmButton = {
            Button(onClick = onConfirmClick) {
                Text(text = stringResource(R.string.label_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.label_close_app))
            }
        }
    )
}

@LocalizationPreview
@Composable
fun RequestCameraPermissionRationaleDialogPreview() {
    AppTheme {
        CameraPermissionRationaleDialog({}, {})
    }
}
