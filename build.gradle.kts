plugins {
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.gradleDownload) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.dependencyVersions)
}

// Todo: do we really need this?
tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
