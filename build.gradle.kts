plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.gradleDownload) apply false
    alias(libs.plugins.ksp) apply false

    alias(libs.plugins.versions)
    alias(libs.plugins.gradleDoctor)
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
