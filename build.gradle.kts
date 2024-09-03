plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.test.coverage) apply true
    alias(libs.plugins.dependency.graph.generator) apply true
    alias(libs.plugins.android.library) apply false
}

rootCoverage {
    generateCsv = true
}

tasks.register("preCommit") {
    dependsOn("app:ktlintFormat", "rootCoverageReport")
}

tasks.register("projectDependencyGraph") {
    dependsOn("generateProjectDependencyGraph")

    doLast {
        val sourceFile = file("$rootDir/build/reports/project-dependency-graph/project-dependency-graph.svg")
        val targetFile = file("$rootDir/structure")
        if (sourceFile.exists()) {
            copy {
                from(sourceFile)
                into(targetFile)
            }
        }
    }
}
