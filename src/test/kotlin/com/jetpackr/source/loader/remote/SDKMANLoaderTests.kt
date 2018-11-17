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
import org.amshove.kluent.shouldContainAll

class SDKMANLoaderTests : StringSpec() {
    val log = KotlinLogging.logger {}

    val GRADLE_URL = "https://api.sdkman.io/2/candidates/gradle/Linux64/versions/all"
    val MAVEN_URL = "https://api.sdkman.io/2/candidates/maven/Linux64/versions/all"
    val SBT_URL = "https://api.sdkman.io/2/candidates/sbt/Linux64/versions/all"

    val mockEngine = MockEngine {
        when (url.fullUrl) {
            GRADLE_URL -> {
                MockHttpResponse(
                        call,
                        HttpStatusCode.OK,
                        ByteReadChannel("4.5.1,4.6,4.7,4.8,4.8.1,4.9,5.0-rc-1,5.0-rc-2".toByteArray(Charsets.UTF_8)),
                        headersOf("Content-Type" to listOf(Text.Plain.toString()))
                )
            }
            MAVEN_URL -> {
                MockHttpResponse(
                        call,
                        HttpStatusCode.OK,
                        ByteReadChannel("3.3.9,3.5.0,3.5.2,3.5.3,3.5.4,3.6.0".toByteArray(Charsets.UTF_8)),
                        headersOf("Content-Type" to listOf(Text.Plain.toString()))
                )
            }
            SBT_URL -> {
                MockHttpResponse(
                        call,
                        HttpStatusCode.OK,
                        ByteReadChannel("1.2.4,1.2.5,1.2.6".toByteArray(Charsets.UTF_8)),
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
        "return versions for 'Gradle'" {
            runBlocking {
                val releases = loader.load(GRADLE_URL)
                releases.size `should be equal to` 8
                releases.values shouldContainAll listOf("5.0-rc-1", "4.5.1", "4.8.1", "4.8")
            }
        }

        "return versions for 'Maven'" {
            runBlocking {
                val releases = loader.load(MAVEN_URL)
                releases.size `should be equal to` 6
                releases.values shouldContainAll listOf("3.5.3", "3.3.9", "3.6.0")
            }
        }

        "return versions for 'sbt'" {
            runBlocking {
                val releases = loader.load(SBT_URL)
                releases.size `should be equal to` 3
                releases.values shouldContainAll listOf("1.2.6", "1.2.5", "1.2.4")
            }
        }
    }
}