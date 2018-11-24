package com.jetpackr.source.loader.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.get

class SDKMANLoader(client: HttpClient): RemoteLoader(client) {
    override suspend fun doLoad(url: String): List<Pair<String, String>> {
        return client.get<String>(url)
                .split(",")
                .map { Pair(it, it) }
    }
}