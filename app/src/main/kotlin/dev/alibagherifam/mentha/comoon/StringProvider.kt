package dev.alibagherifam.mentha.comoon

import android.content.res.Resources
import androidx.annotation.StringRes

class StringProvider(private val resources: Resources) {
    fun getString(@StringRes resId: Int): String = resources.getString(resId)
    fun getString(
        @StringRes resId: Int,
        vararg args: Any
    ): String = resources.getString(resId, *args)
}
