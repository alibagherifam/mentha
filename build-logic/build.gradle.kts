plugins {
    `kotlin-dsl`
}

group = "dev.alibagherifam.mentha.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.android.gradlePlugin)
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
    }
}
