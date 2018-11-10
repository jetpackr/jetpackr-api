package com.jetpackr

import com.jetpackr.cache.CacheService
import com.jetpackr.generator.GeneratorService
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import org.koin.ktor.ext.inject

fun Route.cache() {
    val cacheService: CacheService by inject()

    get("/") {
        call.respond(cacheService.load())
    }
}

fun Route.generator() {
    val generatorService: GeneratorService by inject()

    post("/") {
        TODO()
    }
}