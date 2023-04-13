plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.detekt.core) apply false
    alias(libs.plugins.gradleDownload) apply false
    alias(libs.plugins.ksp) apply false

    alias(libs.plugins.dependencyVersions)
    alias(libs.plugins.gradleDoctor)
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

tasks.register("detektAll") {
    group = "verification"
    description = "Runs Detekt on every sub-project"
    dependsOn(
        subprojects.filter {
            it.plugins.hasPlugin(libs.plugins.detekt.core.get().pluginId)
        }.map { it.tasks.named("detekt") }
    )
}
