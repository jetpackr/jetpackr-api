package com.jetpackr.common.loader

import com.jetpackr.common.comparator.VersionComparator
import com.jetpackr.common.filter.VersionFilter
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