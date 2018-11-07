package com.jetpackr.common.loader

import com.jetpackr.common.comparator.VersionComparator
import com.jetpackr.common.data.parameter.Option
import com.jetpackr.common.data.response.GitHubResponse
import com.jetpackr.common.filter.VersionFilter
import io.ktor.client.request.get

val GitHubLoader: SourceLoader = { url, client ->
    client.get<List<GitHubResponse>>(url).mapNotNull{
        if (it.name.startsWith("v"))
            it.name
        else
            null
    }.filter(VersionFilter).sortedWith(VersionComparator).map {
        Option(value = it)
    }
}