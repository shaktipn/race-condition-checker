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
            call.respondText("\nCurrent value (path1): $globalVariable")
        }
        get("/updateGlobalVariable2"){
            ++globalVariable
            call.respondText("\nCurrent value (path2): $globalVariable")
        }
        get("/updateGlobalVariable3"){
            ++globalVariable
            call.respondText("\nCurrent value (path3): $globalVariable")
        }
        get("/finalGlobalVariableValue"){
            call.respondText("\nFinal value of Global variable: $globalVariable")
        }
    }
}
