package com.jetpackr.source.loader.remote

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.jetpackr.common.fullUrl
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
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain all`

class NPMRegistryLoaderTests : StringSpec() {
    val log = KotlinLogging.logger {}

    val ANGULAR = Pair(
            "https://registry.npmjs.com/@angular/cli",
            """
                {
                    "time": {
                        "modified": "2018-11-14T21:18:17.281Z",
                        "created": "2017-02-01T22:39:48.654Z",
                        "1.0.0-beta.28.3": "2017-02-01T22:39:48.654Z",
                        "1.0.0-beta.29": "2017-02-02T02:31:47.645Z",
                        "1.7.0-rc.0": "2018-02-08T02:22:00.345Z",
                        "1.7.0": "2018-02-15T22:49:59.475Z",
                        "6.0.0-beta.1": "2018-02-15T23:56:11.710Z",
                        "6.0.0-beta.2": "2018-02-16T00:24:56.772Z",
                        "6.2.7": "2018-11-05T17:57:08.085Z",
                        "7.1.0-beta.1": "2018-11-08T22:17:28.355Z",
                        "7.0.5": "2018-11-08T22:25:39.991Z",
                        "7.0.6": "2018-11-14T20:59:42.861Z",
                        "7.1.0-rc.0": "2018-11-14T21:18:14.626Z"
                    }
                }
            """.trimIndent()
    )

    val REACT = Pair(
            "https://registry.npmjs.com/create-react-app",
            """
                {
                    "time": {
                        "modified": "2018-11-01T03:30:01.152Z",
                        "created": "2016-07-17T14:18:51.006Z",
                        "0.0.0": "2016-07-17T14:18:51.006Z",
                        "0.1.0": "2016-07-22T15:38:28.716Z",
                        "0.2.0-alpha.1": "2016-07-27T21:05:49.135Z",
                        "1.2.1": "2017-02-28T23:19:56.429Z",
                        "1.3.0": "2017-03-06T19:12:06.374Z",
                        "1.3.0-alpha.58689133": "2017-05-16T08:50:40.003Z",
                        "1.5.0": "2018-01-15T00:53:41.274Z",
                        "2.0.0-next.096703ab": "2018-01-18T03:46:54.525Z",
                        "2.0.0-next.9754a231": "2018-01-18T05:20:58.088Z",
                        "1.5.1": "2018-01-18T12:09:00.552Z"
                    }
                }
            """.trimIndent()
    )

    val VUE = Pair(
            "https://registry.npmjs.com/@vue/cli",
            """
                {
                    "time": {
                        "modified": "2018-11-12T08:36:59.064Z",
                        "created": "2018-01-25T16:36:33.999Z",
                        "3.0.0-alpha.1": "2018-01-25T16:36:33.999Z",
                        "3.0.0-alpha.2": "2018-01-25T17:24:14.257Z",
                        "3.0.0-beta.9": "2018-04-28T02:36:34.886Z",
                        "3.0.0-beta.10": "2018-05-11T04:03:38.571Z",
                        "3.0.0-rc.6": "2018-07-26T22:12:53.094Z",
                        "3.0.0-rc.11": "2018-08-07T15:43:56.686Z",
                        "3.1.0": "2018-11-01T03:18:27.173Z",
                        "3.1.2": "2018-11-12T08:22:05.538Z"
                    }
                }
            """.trimIndent()
    )

    val YEOMAN = Pair(
            "https://registry.npmjs.com/yo",
            """
                {
                    "time": {
                        "modified": "2018-08-20T06:56:35.973Z",
                        "created": "2013-02-04T21:18:35.414Z",
                        "1.0.0-beta.1": "2013-02-04T21:18:37.592Z",
                        "1.0.0-beta.2": "2013-02-14T20:14:35.774Z",
                        "1.7.1": "2016-05-05T18:19:28.061Z",
                        "1.8.0": "2016-05-08T23:21:00.031Z",
                        "2.0.1": "2018-01-25T14:23:56.348Z",
                        "2.0.2": "2018-04-14T08:20:54.858Z"
                    }
                }
            """.trimIndent()
    )

    val mockEngine = MockEngine {
        log.info("url: {}", url.fullUrl)
        when (url.fullUrl) {
            ANGULAR.first -> {
                MockHttpResponse(
                        call,
                        HttpStatusCode.OK,
                        ByteReadChannel(ANGULAR.second.toByteArray(Charsets.UTF_8)),
                        headersOf("Content-Type" to listOf(Application.Json.toString()))
                )
            }
            REACT.first -> {
                MockHttpResponse(
                        call,
                        HttpStatusCode.OK,
                        ByteReadChannel(REACT.second.toByteArray(Charsets.UTF_8)),
                        headersOf("Content-Type" to listOf(Application.Json.toString()))
                )
            }
            VUE.first -> {
                MockHttpResponse(
                        call,
                        HttpStatusCode.OK,
                        ByteReadChannel(VUE.second.toByteArray(Charsets.UTF_8)),
                        headersOf("Content-Type" to listOf(Application.Json.toString()))
                )
            }
            YEOMAN.first -> {
                MockHttpResponse(
                        call,
                        HttpStatusCode.OK,
                        ByteReadChannel(YEOMAN.second.toByteArray(Charsets.UTF_8)),
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

    val loader = NPMRegistryLoader(client)

    init {
        "return versions for 'Angular'" {
            runBlocking {
                val versions = loader.load(ANGULAR.first)
                log.info("versions: {}", versions)

                versions.size `should be equal to` 6
                versions.map { it.second } `should contain all` listOf("6.2.7", "7.0.5", "7.0.6", "1.7.0")

                Any()
            }
        }

        "return versions for 'React'" {
            runBlocking {
                val versions = loader.load(REACT.first)
                log.info("versions: {}", versions)

                versions.size `should be equal to` 6
                versions.map { it.second } `should contain all` listOf("1.3.0", "1.5.1", "1.2.1")

                Any()
            }
        }

        "return versions for 'Vue.js'" {
            runBlocking {
                val versions = loader.load(VUE.first)
                log.info("versions: {}", versions)

                versions.size `should be equal to` 4
                versions.map { it.second } `should contain all` listOf("3.1.2", "3.1.0")

                Any()
            }
        }

        "return versions for 'Yeoman'" {
            runBlocking {
                val versions = loader.load(YEOMAN.first)
                log.info("versions: {}", versions)
                versions.size `should be equal to` 4
                versions.map { it.second } `should contain all` listOf("2.0.1", "2.0.2", "1.8.0")

                Any()
            }
        }
    }
}