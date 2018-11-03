package com.jetpackr.configuration.parameter.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.jetpackr.configuration.Source
import com.jetpackr.configuration.Source.Type
import com.jetpackr.configuration.Source.Type.DockerHub
import com.jetpackr.configuration.Source.Type.VagrantCloud
import com.jetpackr.configuration.parameter.Option
import com.jetpackr.configuration.parameter.Select
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import java.io.IOException
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.TimeZone

class SelectDeserializer: StdDeserializer<Select>(Select::class.java) {
    private val log = KotlinLogging.logger {}
    private val client = HttpClient(Apache) {
        install(JsonFeature) {
            serializer = JacksonSerializer()
        }
    }

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(parser: JsonParser, context: DeserializationContext): Select = runBlocking{
        log.debug("Deserializing Parameter...")
        // Special logic

        val codec = parser.codec
        val selectNode: JsonNode = codec.readTree(parser)

        val name = selectNode.get("name").asText()
        val label = selectNode.get("label").asText()
        val options: List<Option>

        val sourceNode = selectNode.get("source")

        if (sourceNode != null) {
            val source = codec.treeToValue(sourceNode, Source::class.java)

            log.debug("Source: {}", source)

            options = when (source.type) {
                Type.TimeZone -> TimeZone.getAvailableIDs().mapNotNull { it ->
                    if (!it.startsWith("SystemV"))
                        Option(value = it)
                    else
                        null
                }

                DockerHub -> listOf()

                VagrantCloud -> async {
                    val instant = LocalDate.of(2015, 12, 31)
                            .atStartOfDay()
                            .atZone(ZoneId.systemDefault()).toInstant()

                    val rootNode = client.get<JsonNode>(source.url as String)

                    rootNode.get("boxes").elements().asSequence().toList().mapNotNull {
                        val createdAt = codec.treeToValue(it.get("created_at"), Date::class.java)
                        val afterDate = Date.from(instant)

                        if (it.get("name").asText().endsWith("64") && createdAt.after(afterDate))
                            Option(it.get("short_description").asText(), it.get("tag").asText())
                        else
                            null
                    }
                }.await()


                else -> throw Exception("'source' must be one of ${Type.values().joinToString { it.name }}")
            }
        } else {
            val optionsNode = selectNode.get("options")

            if (optionsNode != null) {
                options = optionsNode.elements().asSequence().toList().mapNotNull {
                    Option(it.get("label").asText(), it.get("value").asText())
                }
            } else
                throw Exception("'Select' parameter requires 'name', 'label', and one of 'options' or 'source'")
        }

        Select(name = name, label = label, options = options)
    }
}