package com.jetpackr

import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.jetpackr.configuration.Option
import com.jetpackr.configuration.Parameter
import com.jetpackr.configuration.machine.Machine
import com.jetpackr.configuration.machine.Timezone
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
import org.koin.ktor.ext.installKoin
import org.koin.log.Logger.SLF4JLogger

val log = KotlinLogging.logger {}

fun Application.module() {

    installKoin(
            listOf(),
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
        val config = environment.config

        val jetpackrConfig = config.config("jetpackr")
        val machineConfig = jetpackrConfig.config("machine")
        val boxConfig = machineConfig.config("box")
        val memoryConfig = machineConfig.config("memory")
        val timezoneConfig = machineConfig.config("timezone")
        val synchronizationConfig = machineConfig.config("synchronization")

        val machine = Machine(
                name = machineConfig.property("name").getString(),
                label = machineConfig.property("label").getString(),
                description = machineConfig.property("description").getString(),
                box = Parameter(
                        name = boxConfig.property("name").getString(),
                        label = boxConfig.property("label").getString()
                ),
                memory = Parameter(
                        name = memoryConfig.property("name").getString(),
                        label = memoryConfig.property("label").getString()
                ),
                timezone = Timezone(
                        name = timezoneConfig.property("name").getString(),
                        label = timezoneConfig.property("label").getString()
                ),
                synchorization = Parameter(
                        name = timezoneConfig.property("name").getString(),
                        label = timezoneConfig.property("label").getString(),
                        options = synchronizationConfig.configList("options").map {
                            Option(
                                    label = it.property("label").getString(),
                                    value = it.property("value").getString()
                            )
                        }
                )
        )


        log.debug("config: {}", machine )
    })




}

fun main(args: Array<String>): Unit {
    EngineMain.main(args)
}