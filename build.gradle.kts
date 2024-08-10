import com.android.build.gradle.internal.tasks.factory.registerTask

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.test.coverage) apply true
}

rootCoverage {
    generateCsv = true
}

tasks.register("preCommit") {
    dependsOn("app:ktlintFormat", "rootCoverageReport")
}
