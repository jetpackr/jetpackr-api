package com.jetpackr

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.jetpackr.common.CommonModule
import com.jetpackr.core.CoreModule
import com.jetpackr.core.generator
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.jackson.jackson
import io.ktor.routing.routing
import io.ktor.server.engine.ShutDownUrl
import io.ktor.server.netty.EngineMain
import mu.KotlinLogging
import org.koin.ktor.ext.installKoin
import org.koin.log.Logger.SLF4JLogger

val log = KotlinLogging.logger {}

fun Application.module() {
    installKoin(
            listOf(CommonModule, CoreModule),
            logger = SLF4JLogger()
    )

    install(DefaultHeaders)
    install(Compression)
    install(CallLogging)

    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.INDENT_OUTPUT, true)
            registerModule(JavaTimeModule())  // support java.time.* types
            setSerializationInclusion(Include.NON_NULL)
            setDefaultPrettyPrinter(DefaultPrettyPrinter().apply {
                indentArraysWith(DefaultPrettyPrinter.FixedSpaceIndenter.instance)
                indentObjectsWith(DefaultIndenter("  ", "\n"))
            })
        }
    }

    install(ShutDownUrl.ApplicationCallFeature) {
        // The URL that will be intercepted
        shutDownUrl = "/shutdown"
        // A function that will be executed to get the exit code of the process
        exitCodeSupplier = { 0 } // ApplicationCall.() -> Int
    }

    routing {
        generator()
    }

//    @UseExperimental(KtorExperimentalAPI::class)
//    environment.monitor.subscribe(ApplicationStarted, handler = {
//        val generatorService: GeneratorService by inject()
//        val jetpackr = generatorService.load()
//
//        val jsonMapper = ObjectMapper()
//
//        jsonMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
//        jsonMapper.setDefaultPrettyPrinter(DefaultPrettyPrinter().apply {
//            indentArraysWith(DefaultPrettyPrinter.FixedSpaceIndenter.instance)
//            indentObjectsWith(DefaultIndenter("  ", "\n"))
//        })
//
//        jsonMapper.setSerializationInclusion(Include.NON_NULL)
//
//        log.info("Let see what we have here!!!: {}", jsonMapper.writeValueAsString(jetpackr))
//    })
}

fun main(args: Array<String>): Unit {
    EngineMain.main(args)
}