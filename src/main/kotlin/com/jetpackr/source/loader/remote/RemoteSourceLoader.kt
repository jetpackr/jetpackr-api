package com.jetpackr.source.loader.remote

import com.jetpackr.source.loader.SourceLoader
import io.ktor.client.HttpClient

abstract class RemoteSourceLoader(protected val client: HttpClient): SourceLoader() {
    final override suspend fun load(): List<String> = TODO("Not implemented")

    final override suspend fun load(url: String): List<String> {
        return doLoad(url)
                .sortedWith(SourceComparator)
                .filter(SourceFilter)
    }

    abstract suspend fun doLoad(url: String): List<String>
}