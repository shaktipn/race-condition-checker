package com.training.shakti

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration
import kotlin.time.measureTime

fun main() {
    val numberOfCoroutines = config.getInt("coroutineConfig.numberOfCoroutines")
    println(
        """How do you want to test the race-condition?
        |1. create N Coroutines for each route
        |2. create coroutines for all routes, N times
        |Enter your choice: """.trimMargin()
    )
    val userChoice = readlnOrNull()?.toInt()
    val timeTakenToExecuteAllCoroutines =
        runBlockingAndMeasureTimeTaken {
            if (userChoice == 1) {
                createNCoroutinesForEachRoute(numberOfCoroutines = numberOfCoroutines)
            } else {
                createCoroutinesForAllRoutesNTimes(numberOfCoroutines = numberOfCoroutines)
            }
        }
    showDurationAndResponse(
        duration = timeTakenToExecuteAllCoroutines,
        url = "http://0.0.0.0:8080/getGlobalVariableValueAndReset"
    )
}

fun showDurationAndResponse(duration: Duration, url: String) {
    //show time taken
    println("\nCoroutines finished execution in ${duration.inWholeMilliseconds / 1000.0F} seconds.")
    //show final value
    val result = ProcessBuilder("curl", url)
        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
        .start()
        .waitFor()
    assert(result == 0) { "The process to execute URL failed." }
}

fun runBlockingAndMeasureTimeTaken(block: CoroutineScope.() -> Unit): Duration {
    return runBlocking {
        measureTime {
            block()
        }
    }
}

fun CoroutineScope.createNCoroutinesForEachRoute(numberOfCoroutines: Int) {
    (1..NUMBER_OF_ROUTES).forEach { routeNumber ->
        val processToTriggerRoute = ProcessBuilder(
            "curl",
            "http://0.0.0.0:8080/updateGlobalVariable$routeNumber"
        ).redirectOutput(ProcessBuilder.Redirect.INHERIT)
        (1..numberOfCoroutines).forEach { _ ->
            launch(Dispatchers.Default) {
                assert(processToTriggerRoute.start().waitFor() == 0) { "Process to trigger route $routeNumber failed" }
            }
        }
    }
}

fun CoroutineScope.createCoroutinesForAllRoutesNTimes(numberOfCoroutines: Int) {
    val processToTriggerRoute1ByCurl = ProcessBuilder(
        "curl",
        "http://0.0.0.0:8080/updateGlobalVariable1"
    ).redirectOutput(ProcessBuilder.Redirect.INHERIT)
    val processToTriggerRoute2ByCurl = ProcessBuilder(
        "curl",
        "http://0.0.0.0:8080/updateGlobalVariable2"
    ).redirectOutput(ProcessBuilder.Redirect.INHERIT)
    val processToTriggerRoute3ByCurl = ProcessBuilder(
        "curl",
        "http://0.0.0.0:8080/updateGlobalVariable3"
    ).redirectOutput(ProcessBuilder.Redirect.INHERIT)
    (1..numberOfCoroutines).forEach { _ ->
        launch(Dispatchers.Default) {
            assert(processToTriggerRoute1ByCurl.start().waitFor() == 0) { "Process to trigger route 1 failed" }
        }
        launch(Dispatchers.Default) {
            assert(processToTriggerRoute2ByCurl.start().waitFor() == 0) { "Process to trigger route 2 failed" }
        }
        launch(Dispatchers.Default) {
            assert(processToTriggerRoute3ByCurl.start().waitFor() == 0) { "Process to trigger route 3 failed" }
        }
    }
}