package dev.alibagherifam.mentha.permission

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissionScaffold(
    permission: String,
    rationaleDialogTitle: String,
    rationaleDialogMessage: String,
    onPermissionDeny: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) = Box(modifier.fillMaxSize()) {
    val permissionState = rememberPermissionState(permission)
    if (permissionState.status.isGranted) {
        content()
    } else {
        RequestPermissionDialog(
            title = rationaleDialogTitle,
            message = rationaleDialogMessage,
            onConfirmClick = { permissionState.launchPermissionRequest() },
            onDismissRequest = onPermissionDeny
        )
    }
}
