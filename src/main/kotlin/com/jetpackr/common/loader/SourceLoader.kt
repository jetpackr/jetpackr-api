package com.jetpackr.common.loader

import io.ktor.client.HttpClient

abstract class SourceLoader(protected val client: HttpClient) {
    abstract suspend fun load(url: String): List<String>
}