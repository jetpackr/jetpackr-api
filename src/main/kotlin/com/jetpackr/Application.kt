package com.jetpackr

import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.jetpackr.common.CommonModule
import com.jetpackr.configuration.container.Container
import com.jetpackr.configuration.tool.Tool
import com.jetpackr.configuration.machine.Machine
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
            setDefaultPrettyPrinter(DefaultPrettyPrinter().apply {
                indentArraysWith(DefaultPrettyPrinter.FixedSpaceIndenter.instance)
                indentObjectsWith(DefaultIndenter("  ", "\n"))
            })
            registerModule(JavaTimeModule())  // support java.time.* types
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

        val machine = mapper.readValue<Machine>(this::class.java.getResourceAsStream("/jetpackr/machine.yml"))
        val kits = mapper.readValue<Map<String, Tool>>(this::class.java.getResourceAsStream("/jetpackr/kits.yml"))
        val containers = mapper.readValue<Map<String, Container>>(this::class.java.getResourceAsStream("/jetpackr/containers.yml"))

        val jsonMapper = ObjectMapper()
        jsonMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
        jsonMapper.setDefaultPrettyPrinter(DefaultPrettyPrinter().apply {
            indentArraysWith(DefaultPrettyPrinter.FixedSpaceIndenter.instance)
            indentObjectsWith(DefaultIndenter("  ", "\n"))
        })

        log.debug("machine: {}", jsonMapper.writeValueAsString(machine))
        log.debug("kits: {}", jsonMapper.writeValueAsString(kits))
        log.debug("containers: {}", jsonMapper.writeValueAsString(containers))
    })
}

fun main(args: Array<String>): Unit {
    EngineMain.main(args)
}