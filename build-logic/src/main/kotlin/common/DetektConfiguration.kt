package common

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

fun Project.configureWithDetekt(libs: VersionCatalog) {
    pluginManager.apply(libs, "detekt.core")

    extensions.configure<DetektExtension> {
        ignoreFailures = true
        config = files("$rootDir/config/detekt.yml")
        buildUponDefaultConfig = true
    }

    tasks.withType<Detekt>().configureEach {
        jvmTarget = JavaVersion.VERSION_11.toString()
        reports {
            md.required.set(false)
            sarif.required.set(false)
            txt.required.set(false)
            xml.required.set(false)
        }
    }

    dependencies {
        add(
            "detektPlugins",
            libs.findLibrary("detekt-ktlintFormatting").get()
        )
    }
}
