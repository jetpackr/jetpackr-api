package com.jetpackr.source.loader

import com.jetpackr.source.comparator.VersionComparator
import com.jetpackr.source.response.GitHubResponse
import com.jetpackr.source.filter.VersionFilter
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class GitHubLoader(client: HttpClient): SourceLoader(client) {
    override suspend fun load(url: String): List<String> {
        return client.get<List<GitHubResponse>>(url)
                .mapNotNull {
                    if (it.name.startsWith("v"))
                        it.name
                    else
                        null
                }.filter(VersionFilter)
                .sortedWith(VersionComparator)
    }
}