package com.jetpackr.configuration

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import mu.KotlinLogging
import java.io.IOException

class ParameterDeserializer(
         val defaultDeserializer: JsonDeserializer<*>
): StdDeserializer<Parameter>(Parameter::class.java), ResolvableDeserializer {
    private val log = KotlinLogging.logger {}

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(parser: JsonParser, context: DeserializationContext): Parameter {
        log.debug("Deserializing Parameter...")
        // Special logic

        return defaultDeserializer.deserialize(parser, context) as Parameter
    }

    @Throws(JsonMappingException::class)
    override fun resolve(context: DeserializationContext) {
        (defaultDeserializer as ResolvableDeserializer).resolve(context)
    }
}