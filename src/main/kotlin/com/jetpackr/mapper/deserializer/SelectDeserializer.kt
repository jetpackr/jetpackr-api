package com.jetpackr.mapper.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.jetpackr.data.parameter.Option
import com.jetpackr.data.parameter.Select
import com.jetpackr.source.Source
import com.jetpackr.source.SourceService
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import java.io.IOException

class SelectDeserializer(private val sourceService: SourceService): StdDeserializer<Select>(Select::class.java) {
    private val log = KotlinLogging.logger {}


    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(parser: JsonParser, context: DeserializationContext): Select = runBlocking {
        log.debug("Deserializing 'Select' parameter...")

        val codec = parser.codec
        val selectNode: JsonNode = codec.readTree(parser)

        val name = selectNode.get("name").asText()
        val label = selectNode.get("label").asText()
        val full = selectNode.get("full")?.asBoolean() ?: false
        val options: List<Option>

        val sourceNode = selectNode.get("source")

        if (sourceNode != null) {
            val source = codec.treeToValue(sourceNode, Source::class.java)

            log.debug("Source: {}", source)

            options = sourceService.load(source)
        } else {
            val optionsNode = selectNode.get("options")

            if (optionsNode != null) {
                options = optionsNode.elements().asSequence().toList().mapNotNull {
                    Option(it.get("label").asText(), it.get("value").asText())
                }
            } else
                throw Exception("'Select' parameter requires 'name', 'label', and one of 'options' or 'source'")
        }

        Select(name, label, full, options)
    }
}