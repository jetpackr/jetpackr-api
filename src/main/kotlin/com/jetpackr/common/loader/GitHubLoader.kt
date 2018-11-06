package com.jetpackr.common.loader

import com.jetpackr.common.comparator.VersionComparator
import com.jetpackr.common.data.parameter.Option
import com.jetpackr.common.data.response.GitHubResponse
import io.ktor.client.request.get

val GitHubLoader: SourceLoader = { url, client ->
    if (url == null)
        throw Exception("Url cannot be empty")

    client.get<List<GitHubResponse>>(url).mapNotNull{
        if (it.name.startsWith("v"))
            it.name
        else
            null
    }.sortedWith(VersionComparator).map {
        Option(value = it)
    }
}