package com.jetpackr.source.loader

import com.jetpackr.source.comparator.VersionComparator
import com.jetpackr.source.filter.VersionFilter
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class SDKMANLoader(client: HttpClient): SourceLoader(client) {
    override suspend fun load(url: String): List<String> {
        return client.get<String>(url)
                .split(",")
                .filter(VersionFilter)
                .sortedWith(VersionComparator)
    }
}