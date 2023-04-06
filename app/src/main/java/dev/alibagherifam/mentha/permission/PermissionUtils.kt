package dev.alibagherifam.mentha.permission

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberPermissionStateHolder(permission: String): PermissionStateHolder {
    val context = LocalContext.current
    val stateHolder = remember { PermissionStateHolder(context, permission) }
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = stateHolder::onPermissionResult
    )
    stateHolder.requestPermissionLauncher = requestPermissionLauncher
    return stateHolder
}

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    error("Permissions should be called in the context of an Activity")
}
