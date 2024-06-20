package dev.alibagherifam.build.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType

internal val Project.androidCommonExtension: CommonExtension<*, *, *, *, *, *>
    get() = androidCommonExtensionOrNull
        ?: error("Plugin should be applied on either Android Application or Library")

internal val Project.androidCommonExtensionOrNull: CommonExtension<*, *, *, *, *, *>?
    get() = extensions.let {
        it.findByType<LibraryExtension>() ?: it.findByType<ApplicationExtension>()
    }
