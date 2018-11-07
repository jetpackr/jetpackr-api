package com.jetpackr

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.jetpackr.common.CommonModule
import com.jetpackr.configuration.Jetpackr
import io.ktor.application.Application
import io.ktor.application.ApplicationStarted
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.jackson.jackson
import io.ktor.server.engine.ShutDownUrl
import io.ktor.server.netty.EngineMain
import io.ktor.util.KtorExperimentalAPI
import mu.KotlinLogging
import org.koin.ktor.ext.inject
import org.koin.ktor.ext.installKoin
import org.koin.log.Logger.SLF4JLogger
import java.io.ByteArrayInputStream
import java.io.SequenceInputStream
import java.util.Collections

val log = KotlinLogging.logger {}

fun Application.module() {
    installKoin(
            listOf(CommonModule),
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

    @UseExperimental(KtorExperimentalAPI::class)
    environment.monitor.subscribe(ApplicationStarted, handler = {
        val mapper: ObjectMapper by inject()

        val inputStream = SequenceInputStream(
                Collections.enumeration(
                        setOf(
                                this::class.java.getResourceAsStream("/jetpackr/machine.yml"),
                                ByteArrayInputStream("\n".toByteArray()),
                                this::class.java.getResourceAsStream("/jetpackr/kits.yml"),
                                ByteArrayInputStream("\n".toByteArray()),
                                this::class.java.getResourceAsStream("/jetpackr/containers.yml")
                        )
                )
        )

        val jetpackr = mapper.readValue<Jetpackr>(inputStream)

        val jsonMapper = ObjectMapper()
        jsonMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
        jsonMapper.setDefaultPrettyPrinter(DefaultPrettyPrinter().apply {
            indentArraysWith(DefaultPrettyPrinter.FixedSpaceIndenter.instance)
            indentObjectsWith(DefaultIndenter("  ", "\n"))
        })
        jsonMapper.setSerializationInclusion(Include.NON_NULL)

        log.debug("jetpackr: {}", jsonMapper.writeValueAsString(jetpackr))
    })
}

fun main(args: Array<String>): Unit {
    EngineMain.main(args)
}