package com.training.shakti.plugins

import com.training.shakti.globalVariable
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/updateGlobalVariable1"){
            ++globalVariable
            call.respondText("\nCurrent value (in route 1): $globalVariable")
        }
        get("/updateGlobalVariable2"){
            ++globalVariable
            call.respondText("\nCurrent value (in route 2): $globalVariable")
        }
        get("/updateGlobalVariable3"){
            ++globalVariable
            call.respondText("\nCurrent value (in route 3): $globalVariable")
        }
        get("/getGlobalVariableValueAndReset"){
            val globalVariableValue = globalVariable
            globalVariable = 0
            call.respondText("\nFinal value of Global variable: $globalVariableValue")
        }
    }
}
