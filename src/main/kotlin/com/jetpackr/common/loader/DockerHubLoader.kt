package com.jetpackr.common.loader

import com.jetpackr.common.comparator.VersionComparator
import com.jetpackr.common.data.response.DockerHubResponse
import com.jetpackr.common.filter.VersionFilter
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