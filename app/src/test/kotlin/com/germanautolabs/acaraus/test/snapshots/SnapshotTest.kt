package com.germanautolabs.acaraus.test.snapshots

import androidx.compose.runtime.currentComposer
import androidx.compose.ui.tooling.preview.Preview
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.germanautolabs.acaraus.test.main.rules.CoroutinesTestRule
import com.germanautolabs.acaraus.test.main.rules.KoinUnitTestRule
import io.github.classgraph.ClassGraph
import io.github.classgraph.MethodInfo
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import java.lang.reflect.Method
import java.lang.reflect.Modifier

class SnapshotTest : KoinTest {

    @get:Rule
    val koinUnitTestRule = KoinUnitTestRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    val paparazzi = Paparazzi(
        theme = "android:Theme.Material.Light.NoActionBar",
        //  showSystemUi = true,
        deviceConfig = DeviceConfig.PIXEL_6_PRO,
    )

    @Test
    fun generateSnapshot() {
        val methods = findAllPreviewFunctionsAndInvoke("com.germanautolabs.acaraus.screens")

        for (method in methods) {
            paparazzi.snapshot(name = method.name) {
                method.invoke(null, currentComposer, 0)
            }
        }
    }
}

fun findAllPreviewFunctionsAndInvoke(packageName: String): List<Method> {
    val previewFunctions = mutableListOf<Method>()

    ClassGraph()
        .enableAllInfo()
        .acceptPackages(packageName)
        .scan().use { scanResult ->
            scanResult.getClassesWithMethodAnnotation(Preview::class.java.name)
                .flatMap { it.methodInfo }
                .filter { it.hasAnnotation(Preview::class.java.name) }
                .forEach { methodInfo ->
                    filterPreview(methodInfo, previewFunctions::add)
                }
        }

    return previewFunctions
}

fun filterPreview(methodInfo: MethodInfo, add: (method: Method) -> Unit) {
    val className = methodInfo.className
    val methodName = methodInfo.name

    try {
        val clazz = Class.forName(className)
        val method = clazz.declaredMethods.find { it.name == methodName }
            ?: throw NoSuchMethodException("$methodName in $className")

        method.isAccessible = true // Make the method accessible

        if (method.parameterCount == 2) {
            if (Modifier.isStatic(method.modifiers)) {
                // For static methods (including top-level functions)
                add(method)
            } else {
                // For instance methods
                val instance = clazz.getDeclaredConstructor().newInstance()
                method.invoke(instance)
            }
            println("Successfully invoked: $className.$methodName")
        } else {
            println("Cannot automatically invoke method with parameters: $className.$methodName")
        }
    } catch (e: Exception) {
        println("Error invoking method $className.$methodName: ${e.message}")
        e.printStackTrace() // Print the full stack trace for debugging
    }
}
