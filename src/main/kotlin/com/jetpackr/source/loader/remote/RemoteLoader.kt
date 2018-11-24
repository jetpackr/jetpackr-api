package com.jetpackr.source.loader.remote

import io.ktor.client.HttpClient

abstract class RemoteLoader(protected val client: HttpClient) {
    suspend fun load(url: String): List<Pair<String, String>> {
        return doLoad(url)
                .filter(VersionFilter)
                .sortedWith(VersionComparator)
    }

    abstract suspend fun doLoad(url: String): List<Pair<String, String>>
}