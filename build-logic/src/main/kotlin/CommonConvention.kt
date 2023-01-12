import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun Project.configureAndroidBaseOptions(android: CommonExtension<*, *, *, *>) {
    setSdkVersionBoundaries(android)
    setCompilerVersion(android)
    android.androidResources {
        noCompress("tflite")
    }
}

fun setSdkVersionBoundaries(android: CommonExtension<*, *, *, *>) {
    android.apply {
        compileSdk = 33
        buildToolsVersion = "33.0.1"
        defaultConfig {
            minSdk = 23
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }
}

fun Project.setCompilerVersion(android: CommonExtension<*, *, *, *>) {
    val javaVersion = JavaVersion.VERSION_11
    setJavaVersionForKotlinCompiler(javaVersion)
    android.compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
}

fun Project.setJavaVersionForKotlinCompiler(version: JavaVersion) {
    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = version.toString()
    }
}
