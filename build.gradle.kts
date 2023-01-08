plugins {
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.gradleDownload) apply false
    id("org.jetbrains.kotlin.jvm") version "1.7.20" apply false
}

// Todo: do we really need this?
tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
