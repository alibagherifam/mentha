package common

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun Project.setJvmTargetVersion(android: CommonExtension<*, *, *, *>) {
    val javaVersion = JavaVersion.VERSION_17
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
