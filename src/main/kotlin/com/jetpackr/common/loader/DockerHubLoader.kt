package com.jetpackr.common.loader

import com.jetpackr.common.comparator.VersionComparator
import com.jetpackr.common.data.parameter.Option
import com.jetpackr.common.data.response.DockerHubResponse
import com.jetpackr.common.filter.VersionFilter
import io.ktor.client.request.get

val DockerHubLoader: SourceLoader = { client, url ->
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

    versions.filter(VersionFilter).sortedWith(VersionComparator).map {
        Option(value = it)
    }
}