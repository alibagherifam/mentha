package com.alibagherifam.mentha.permission

import android.Manifest
import android.app.Activity

enum class PermissionState {
    NOT_REQUESTED,
    GRANTED,
    SHOULD_SHOW_RATIONALE,
    NEVER_ASK_AGAIN;

    companion object {
        fun of(
            activity: Activity,
            isRequested: Boolean,
            isGranted: Boolean
        ): PermissionState {
            val shouldShowRationale = !isGranted && activity
                .shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
            return when {
                isGranted -> PermissionState.GRANTED
                shouldShowRationale -> PermissionState.SHOULD_SHOW_RATIONALE
                isRequested -> PermissionState.NEVER_ASK_AGAIN
                else -> PermissionState.NOT_REQUESTED
            }
        }
    }
}
