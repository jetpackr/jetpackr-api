package com.jetpackr.source.loader.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.get

class SDKMANLoader(client: HttpClient): RemoteSourceLoader(client) {
    override suspend fun doLoad(url: String): List<String> {
        return client.get<String>(url)
                .split(",")
                .filter(SourceFilter)
                .sortedWith(SourceComparator)
    }
}