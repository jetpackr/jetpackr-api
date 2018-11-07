package com.jetpackr.core

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import org.koin.ktor.ext.inject

fun Route.generator() {
    val generatorService: GeneratorService by inject()

    get("/") {
        call.respond(generatorService.load())
    }
}