package com.jetpackr.common.loader

import com.jetpackr.common.comparator.VersionComparator
import com.jetpackr.common.data.response.GitHubResponse
import com.jetpackr.common.filter.VersionFilter
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