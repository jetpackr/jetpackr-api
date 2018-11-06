package com.jetpackr.common.loader

import com.jetpackr.common.comparator.VersionComparator
import com.jetpackr.common.data.parameter.Option
import io.ktor.client.request.get

val SDKMANLoader: SourceLoader = { url, client ->
    if (url == null)
        throw Exception("Url cannot be empty")

    client.get<String>(url).split(",").sortedWith(VersionComparator).map {
        Option(value = it)
    }
}