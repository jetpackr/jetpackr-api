package com.jetpackr.source.loader

import io.ktor.client.HttpClient

abstract class SourceLoader(protected val client: HttpClient) {
    suspend fun load(url: String): List<String> {
        return doLoad(url)
                .sortedWith(SourceComparator)
                .filter(SourceFilter)
    }

    abstract suspend fun doLoad(url: String): List<String>
}