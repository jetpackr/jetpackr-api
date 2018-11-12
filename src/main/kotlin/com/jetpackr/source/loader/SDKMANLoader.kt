package com.jetpackr.source.loader

import io.ktor.client.HttpClient
import io.ktor.client.request.get

class SDKMANLoader(client: HttpClient): SourceLoader(client) {
    override suspend fun doLoad(url: String): List<String> {
        return client.get<String>(url)
                .split(",")
    }
}