plugins {
    `kotlin-dsl`
}

group = "com.alibagherifam.menta.buildlogic"

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
            id = "com.alibagherifam.mentha.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "com.alibagherifam.mentha.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
    }
}
