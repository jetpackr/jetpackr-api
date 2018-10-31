package com.jetpackr

import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.jetpackr.configuration.Parameter
import com.jetpackr.configuration.ParameterDeserializer
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
        val deserializerModule = SimpleModule()

        deserializerModule.setDeserializerModifier(object : BeanDeserializerModifier() {
            override fun modifyDeserializer(config: DeserializationConfig, beanDesc: BeanDescription, deserializer: JsonDeserializer<*>): JsonDeserializer<*> {
                return if (beanDesc.beanClass === Parameter::class.java) ParameterDeserializer(deserializer) else deserializer
            }
        })

        val mapper = ObjectMapper(YAMLFactory()) // Enable YAML parsing
        mapper.registerModule(deserializerModule)
        mapper.registerModule(KotlinModule()) // Enable Kotlin support

        val machine = mapper.readValue<Machine>(this::class.java.getResourceAsStream("/jetpackr/machine.yml"))

        log.debug("config: {}", machine )
    })




}

fun main(args: Array<String>): Unit {
    EngineMain.main(args)
}