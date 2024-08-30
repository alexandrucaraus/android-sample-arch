package eu.acaraus.news.test.common.previews

import androidx.compose.ui.tooling.preview.Preview
import io.github.classgraph.ClassGraph
import io.github.classgraph.MethodInfo
import java.lang.reflect.Method

/**
 *  this - is the package name and it's sub-packages
 *
 */
fun String.getAListOfAllComposablePreviewMethods(): List<Method> =
    ClassGraph()
        .enableAllInfo()
        .acceptPackages(this)
        .scan().use { scanResult ->
            scanResult.getClassesWithMethodAnnotation(Preview::class.java.name)
                .flatMap { clazz -> clazz.methodInfo }
                .filter { method -> method.hasAnnotation(Preview::class.java.name) }
                .map(::extractMethod)
        }

private fun extractMethod(methodInfo: MethodInfo): Method {
    val clazz = Class.forName(methodInfo.className)
    val method = clazz.declaredMethods.find { it.name == methodInfo.name }
        ?: throw NoSuchMethodException("${methodInfo.className} in ${methodInfo.className} not found")
    method.isAccessible = true
    return method
}
