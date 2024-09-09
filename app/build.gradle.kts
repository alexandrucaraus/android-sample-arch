@file:Suppress("UnstableApiUsage")

import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    kotlin("plugin.parcelize")
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.dependency.graph.generator) apply true
    alias(libs.plugins.paparazzi) apply true
}

dependencies {

    implementation(project(":shared_lib"))
    implementation(project(":features:news"))

    implementation(kotlin("reflect"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialisation.json)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlin.stdlib)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.icons.extended)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.navigation)
    implementation(libs.androidx.compose.navigation.common)
    implementation(libs.androidx.compose.navigation.common.ktx)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.android.compose)
    implementation(libs.koin.annotations)
    ksp(libs.koin.ksp)

    implementation(libs.coil)

    implementation(libs.androidx.browser)

    implementation(libs.ktor)
    implementation(libs.ktor.cio)
    implementation(libs.ktor.logging)
    implementation(libs.ktor.content.negotiation)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.encoding)
    implementation(libs.ktor.okhttp)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.leakcanary.android)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.koin.test)
    testImplementation(libs.turbine)
    testImplementation(libs.classgraph)

    testFixturesImplementation(libs.kotlin.stdlib)
    testFixturesImplementation(platform(libs.androidx.compose.bom))
    testFixturesImplementation(libs.androidx.compose.ui)
}

android {
    namespace = libs.versions.appPackageId.get()
    compileSdk = libs.versions.compileSdk.get().toInt()
    compileSdkVersion = "android-${libs.versions.compileSdk.get()}"

    defaultConfig {
        applicationId = libs.versions.appPackageId.get()
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

        val (code, name) = versionCodeAndName()
        versionCode = code
        versionName = name

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            type = "String",
            name = "NEWS_BASE_URL",
            value = quoted(value = "https://newsapi.org/"),
        )
        buildConfigField(
            type = "String",
            name = "NEWS_API_KEY",
            value = buildConfigPipelineVar(
                name = "NEWS_API_KEY",
                defaultValue = localPropertyVar("NEWS_API_KEY"),
            )
        )
    }

    signingConfigs {
        create("release") {
            storeFile = pipelineFile(
                name = "RELEASE_KEY_STORE_FILE",
                defaultPath = localPropertyVar("RELEASE_KEY_STORE_FILE"),
            )
            storePassword = pipelineVar(
                name = "RELEASE_KEY_STORE_PASS",
                defaultValue = localPropertyVar("RELEASE_KEY_STORE_PASS"),
            )
            keyAlias = pipelineVar(
                name = "RELEASE_KEY_ALIAS",
                defaultValue = localPropertyVar("RELEASE_KEY_ALIAS"),
            )
            keyPassword = pipelineVar(
                name = "RELEASE_KEY_PASS",
                defaultValue = localPropertyVar("RELEASE_KEY_PASS"),
            )
        }
        getByName("debug") {
            storeFile = file("$rootDir/config/debugKeyStore/debug.keystore")
            storePassword = "password"
            keyAlias = "androiddebugkey"
            keyPassword = "password"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1,INDEX.LIST}"
        }
    }

    testFixtures {
        enable = true
    }
}

ksp {
    arg("KOIN_CONFIG_CHECK", "true")
}

fun quoted(value: String): String = "\"$value\""

fun buildConfigPipelineVar(name: String, defaultValue: String): String =
    quoted(System.getenv(name) ?: defaultValue)

fun pipelineVar(name: String, defaultValue: String = "none"): String =
    System.getenv(name) ?: defaultValue

fun pipelineFile(name: String, defaultPath: String = "none"): File =
    file(System.getenv(name) ?: defaultPath)

fun localPropertyVar(name: String, defaultValue: String = "none"): String {
    val properties = Properties()
        file("$rootDir/local.properties")
            .takeIf { it.exists() }
            ?.inputStream()
            .use { stream -> properties.load(stream) }
    return properties.getProperty(name) ?: defaultValue
}

fun versionCodeAndName(): Pair<Int, String> {
    val timestamp = (System.currentTimeMillis() / 1000).toInt()
    return timestamp to "$timestamp"
}
