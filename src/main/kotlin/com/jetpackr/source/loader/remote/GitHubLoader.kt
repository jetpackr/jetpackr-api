package com.jetpackr.source.loader.remote

import com.jetpackr.source.response.GitHubResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class GitHubLoader(client: HttpClient): RemoteLoader(client) {
    override suspend fun doLoad(url: String): List<Pair<String, String>> {
        return client.get<List<GitHubResponse>>(url)
                .mapNotNull {
                    if (it.name.startsWith("v"))
                        it.name
                    else
                        null
                }
                .map { Pair(it, it) }
    }
}