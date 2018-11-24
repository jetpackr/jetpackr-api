package com.jetpackr.source.loader.remote

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.jetpackr.common.fullUrl
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.specs.StringSpec
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockHttpResponse
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.http.ContentType.Application
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.amshove.kluent.`should contain all`

class DockerHubLoaderTests : StringSpec() {
    private val log = KotlinLogging.logger {}

    private val mysql = Pair(
            "https://registry.hub.docker.com/v2/repositories/library/mysql/tags",
            """
                {
                    "next": "https://registry.hub.docker.com/v2/repositories/library/mysql/tags/?page=2",
                    "results": [
                        { "name": "8.0.4" },
                        { "name": "8.0.1" }
                    ]
                }
            """.trimIndent()
    )

    private val mysqlNextPage2 = Pair(
            "https://registry.hub.docker.com/v2/repositories/library/mysql/tags/?page=2",
            """
                {
                    "results": [
                        { "name": "5.7.14" },
                        { "name": "5.5.61" }
                    ]
                }
            """.trimIndent()
    )

    private val elasticSearch = Pair(
            "https://registry.hub.docker.com/v2/repositories/library/elasticsearch/tags",
            """
                {
                    "next": "https://registry.hub.docker.com/v2/repositories/library/elasticsearch/tags/?page=2",
                    "results": [
                        { "name": "6.5.0" },
                        { "name": "6.4.2" }
                    ]
                }
            """.trimIndent()
    )

    private val elasticSearchNextPage2 = Pair(
            "https://registry.hub.docker.com/v2/repositories/library/elasticsearch/tags/?page=2",
            """
                {
                    "next": "https://registry.hub.docker.com/v2/repositories/library/elasticsearch/tags/?page=3",
                    "results": [
                        { "name": "5.6.10" },
                        { "name": "5.6.9" }
                    ]
                }
            """.trimIndent()
    )

    private val elasticSearchNextPage3 = Pair(
            "https://registry.hub.docker.com/v2/repositories/library/elasticsearch/tags/?page=3",
            """
                {
                    "results": [
                        { "name": "2.4.6" },
                        { "name": "2.4.5" }
                    ]
                }
            """.trimIndent()
    )

    private val rabbitMq = Pair(
            "https://registry.hub.docker.com/v2/repositories/library/rabbitmq/tags",
            """
                {
                    "results": [
                        { "name": "3.7.8-management-alpine" },
                        { "name": "3.7.7-alpine" },
                        { "name": "3.7.6" }
                    ]
                }
            """.trimIndent()
    )

    val mockEngine = MockEngine {
        log.info("url: {}", url.fullUrl)
        when (url.fullUrl) {
            mysql.first -> {
                MockHttpResponse(
                        call,
                        HttpStatusCode.OK,
                        ByteReadChannel(mysql.second.toByteArray(Charsets.UTF_8)),
                        headersOf("Content-Type" to listOf(Application.Json.toString()))
                )
            }
            mysqlNextPage2.first -> {
                MockHttpResponse(
                        call,
                        HttpStatusCode.OK,
                        ByteReadChannel(mysqlNextPage2.second.toByteArray(Charsets.UTF_8)),
                        headersOf("Content-Type" to listOf(Application.Json.toString()))
                )
            }
            elasticSearch.first -> {
                MockHttpResponse(
                        call,
                        HttpStatusCode.OK,
                        ByteReadChannel(elasticSearch.second.toByteArray(Charsets.UTF_8)),
                        headersOf("Content-Type" to listOf(Application.Json.toString()))
                )
            }
            elasticSearchNextPage2.first -> {
                MockHttpResponse(
                        call,
                        HttpStatusCode.OK,
                        ByteReadChannel(elasticSearchNextPage2.second.toByteArray(Charsets.UTF_8)),
                        headersOf("Content-Type" to listOf(Application.Json.toString()))
                )
            }
            elasticSearchNextPage3.first -> {
                MockHttpResponse(
                        call,
                        HttpStatusCode.OK,
                        ByteReadChannel(elasticSearchNextPage3.second.toByteArray(Charsets.UTF_8)),
                        headersOf("Content-Type" to listOf(Application.Json.toString()))
                )
            }
            rabbitMq.first -> {
                MockHttpResponse(
                        call,
                        HttpStatusCode.OK,
                        ByteReadChannel(rabbitMq.second.toByteArray(Charsets.UTF_8)),
                        headersOf("Content-Type" to listOf(Application.Json.toString()))
                )
            }
            else -> {
                error("Unhandled ${url.fullUrl}")
            }
        }
    }

    val client = HttpClient(mockEngine) {
        install(JsonFeature) {
            serializer = JacksonSerializer {
                configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
            }
        }
    }

    val loader = DockerHubLoader(client)

    init {
        "load MySQL's versions" {
            runBlocking {
                val tags = loader.load(mysql.first)
                log.info("releases: {}", tags)

                tags shouldHaveSize 4
                tags.map { it.second } `should contain all` listOf("5.7.14", "8.0.4")

                Any()
            }
        }

        "load ElasticSearch's versions" {
            runBlocking {
                val versions = loader.load(elasticSearch.first)
                log.info("releases: {}", versions)

                versions shouldHaveSize 6
                versions.map { it.second } `should contain all` listOf("2.4.5", "6.5.0", "5.6.9")

                Any()
            }
        }

        "load RabbitMQ's versions" {
            runBlocking {
                val versions = loader.load(rabbitMq.first)
                log.info("releases: {}", versions)

                versions shouldHaveSize 3
                versions.map { it.second } `should contain all` listOf("3.7.8-management-alpine", "3.7.7-alpine", "3.7.6")

                Any()
            }

        }
    }
}