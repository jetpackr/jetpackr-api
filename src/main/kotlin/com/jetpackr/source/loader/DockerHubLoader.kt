package com.jetpackr.source.loader

import com.jetpackr.source.common.SourceComparator
import com.jetpackr.source.common.SourceFilter
import com.jetpackr.source.response.DockerHubResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class DockerHubLoader(client: HttpClient): RemoteSourceLoader(client) {
    override suspend fun doLoad(url: String): List<String> {
        val versions = mutableListOf<String>()
        var response = client.get<DockerHubResponse>(url)

        versions += response.results.map {
            it["name"] as String
        }

        while (response.next != null) {
            response = client.get(response.next!!)
            versions += response.results.map {
                it["name"] as String
            }
        }

        return versions.filter(SourceFilter)
                .sortedWith(SourceComparator)
    }
}