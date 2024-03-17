package com.training.shakti

import com.training.shakti.plugins.*
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureRouting()
}

internal val config: Config = ConfigFactory.load("local.conf")
internal const val NUMBER_OF_ROUTES = 3
internal var globalVariable = 0 //we will update this variable in coroutines
