package com.jetpackr.source.loader.remote

import com.jetpackr.source.response.DockerHubResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class DockerHubLoader(client: HttpClient): RemoteLoader(client) {
    override suspend fun doLoad(url: String): List<Pair<String, String>> {
        val tags = mutableListOf<String>()
        var response = client.get<DockerHubResponse>(url)

        tags += response.results.map {
            it["name"] as String
        }

        while (response.next != null) {
            response = client.get(response.next!!)
            tags += response.results.map {
                it["name"] as String
            }
        }

        return tags.map { Pair(it, it) }
    }
}