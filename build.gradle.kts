plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.test.coverage) apply true
    alias(libs.plugins.dependency.graph.generator) apply true
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("app.cash.paparazzi:paparazzi-gradle-plugin:1.3.4")
    }
}

rootCoverage {
    generateCsv = true
}

tasks.register("preCommit") {
    dependsOn("app:ktlintFormat", "rootCoverageReport")
}
