package dev.alibagherifam.mentha.permission

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
                isGranted -> GRANTED
                shouldShowRationale -> SHOULD_SHOW_RATIONALE
                isRequested -> NEVER_ASK_AGAIN
                else -> NOT_REQUESTED
            }
        }
    }
}
