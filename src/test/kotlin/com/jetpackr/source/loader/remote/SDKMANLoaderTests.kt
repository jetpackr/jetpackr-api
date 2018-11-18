package com.jetpackr.source.loader.remote

import com.jetpackr.common.fullUrl
import io.kotlintest.specs.StringSpec
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockHttpResponse
import io.ktor.http.ContentType.Text
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain all`

class SDKMANLoaderTests : StringSpec() {
    val log = KotlinLogging.logger {}

    val GRADLE = Pair(
            "https://api.sdkman.io/2/candidates/gradle/Linux64/versions/all",
            "4.5.1,4.6,4.7,4.8,4.8.1,4.9,5.0-rc-1,5.0-rc-2"
    )

    val MAVEN = Pair(
            "https://api.sdkman.io/2/candidates/maven/Linux64/versions/all",
            "3.3.9,3.5.0,3.5.2,3.5.3,3.5.4,3.6.0"
    )

    val SBT = Pair(
            "https://api.sdkman.io/2/candidates/sbt/Linux64/versions/all",
            "1.2.4,1.2.5,1.2.6"
    )

    val mockEngine = MockEngine {
        when (url.fullUrl) {
            GRADLE.first -> {
                MockHttpResponse(
                        call,
                        HttpStatusCode.OK,
                        ByteReadChannel(GRADLE.second.toByteArray(Charsets.UTF_8)),
                        headersOf("Content-Type" to listOf(Text.Plain.toString()))
                )
            }
            MAVEN.first -> {
                MockHttpResponse(
                        call,
                        HttpStatusCode.OK,
                        ByteReadChannel(MAVEN.second.toByteArray(Charsets.UTF_8)),
                        headersOf("Content-Type" to listOf(Text.Plain.toString()))
                )
            }
            SBT.first -> {
                MockHttpResponse(
                        call,
                        HttpStatusCode.OK,
                        ByteReadChannel(SBT.second.toByteArray(Charsets.UTF_8)),
                        headersOf("Content-Type" to listOf(Text.Plain.toString()))
                )
            }
            else -> {
                error("Unhandled ${url.fullUrl}")
            }
        }
    }

    val client = HttpClient(mockEngine)
    val loader = SDKMANLoader(client)

    init {
        "test releases for 'Gradle'" {
            runBlocking {
                val releases = loader.load(GRADLE.first)
                log.info("releases: {}", releases)
                releases.size `should be equal to` 8
                releases.values `should contain all`  listOf("5.0-rc-1", "4.5.1", "4.8.1", "4.8")
            }
        }

        "test releases for 'Maven'" {
            runBlocking {
                val releases = loader.load(MAVEN.first)

                log.info("releases: {}", releases)

                releases.size `should be equal to` 6
                releases.values `should contain all`  listOf("3.5.3", "3.3.9", "3.6.0")

                Any()
            }
        }

        "test releases for 'sbt'" {
            runBlocking {
                val releases = loader.load(SBT.first)

                log.info("releases: {}", releases)

                releases.size `should be equal to` 3
                releases.values `should contain all` listOf("1.2.6", "1.2.5", "1.2.4")

                Any()
            }
        }
    }
}