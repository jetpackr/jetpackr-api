package com.jetpackr.source.loader

import com.jetpackr.source.comparator.VersionComparator
import com.jetpackr.source.filter.VersionFilter
import com.jetpackr.source.response.DockerHubResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class DockerHubLoader(client: HttpClient): SourceLoader(client) {
    override suspend fun load(url: String): List<String> {
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

        return versions.filter(VersionFilter)
                .sortedWith(VersionComparator)
    }
}