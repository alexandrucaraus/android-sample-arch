package eu.acaraus.core

import org.junit.Test

import org.junit.Assert.*

class EitherTest {

    @Test
    fun test_either_onEachSuccess() {
        val either: Either<String, String> = Either.success("success")
        either.onEachSuccess { success ->
            assertEquals("success", success)
        }.onEachError {
            assertEquals(false) { "Should not be invoked" }
        }
    }

    @Test
    fun test_either_onEachError() {
        val either: Either<String, String> = Either.error("error")
        either.onEachSuccess {
            assertEquals(false) { "Should not be invoked" }
        }.onEachError { error ->
            assertEquals("error", error)
        }
    }

}
