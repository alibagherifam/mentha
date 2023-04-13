plugins {
    `kotlin-dsl`
}

group = "dev.alibagherifam.mentha.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "dev.alibagherifam.mentha.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "dev.alibagherifam.mentha.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("compose") {
            id = "dev.alibagherifam.mentha.compose"
            implementationClass = "ComposeConventionPlugin"
        }
        register("featureLibrary") {
            id = "dev.alibagherifam.mentha.feature"
            implementationClass = "FeatureLibraryConventionPlugin"
        }
    }
}
