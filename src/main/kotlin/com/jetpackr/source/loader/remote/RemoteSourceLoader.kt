package com.jetpackr.source.loader.remote

import io.ktor.client.HttpClient

abstract class RemoteSourceLoader(protected val client: HttpClient) {
    suspend fun load(url: String): List<String> {
        return doLoad(url)
                .sortedWith(SourceComparator)
                .filter(SourceFilter)
    }

    abstract suspend fun doLoad(url: String): List<String>
}