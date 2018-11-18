package com.jetpackr.source.loader.remote

import io.ktor.client.HttpClient

abstract class RemoteLoader(protected val client: HttpClient) {
    suspend fun load(url: String): Map<String, String> {
        return doLoad(url)
                .filterKeys(VersionFilter)
                .toSortedMap(VersionComparator)
    }

    abstract suspend fun doLoad(url: String): Map<String, String>
}