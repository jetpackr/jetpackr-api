package com.jetpackr.source

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockHttpResponse
import io.ktor.http.ContentType.Text
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.http.hostWithPort
import kotlinx.coroutines.io.ByteReadChannel

private val Url.hostWithPortIfRequired: String get() = if (port == protocol.defaultPort) host else hostWithPort
private val Url.fullUrl: String get() = "${protocol.name}://$hostWithPortIfRequired$fullPath"

val mockEngine = MockEngine { call -> // this: HttpRequest, call: HttpClientCall
    when (url.fullUrl) {
        "https://example.org/" -> {
            MockHttpResponse(
                    call,
                    HttpStatusCode.OK,
                    ByteReadChannel("Hello World!".toByteArray(Charsets.UTF_8)),
                    headersOf("Content-Type" to listOf(Text.Plain.toString()))
            )
        }
        else -> {
            error("Unhandled ${url.fullUrl}")
        }
    }
}

val client = HttpClient(mockEngine)