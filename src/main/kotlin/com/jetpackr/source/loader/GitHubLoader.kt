package com.jetpackr.source.loader

import com.jetpackr.source.common.SourceComparator
import com.jetpackr.source.common.SourceFilter
import com.jetpackr.source.response.GitHubResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class GitHubLoader(client: HttpClient): RemoteSourceLoader(client) {
    override suspend fun doLoad(url: String): List<String> {
        return client.get<List<GitHubResponse>>(url)
                .mapNotNull {
                    if (it.name.startsWith("v"))
                        it.name
                    else
                        null
                }.filter(SourceFilter)
                .sortedWith(SourceComparator)
    }
}