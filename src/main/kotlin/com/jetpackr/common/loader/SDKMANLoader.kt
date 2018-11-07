package com.jetpackr.common.loader

import com.jetpackr.common.comparator.VersionComparator
import com.jetpackr.common.data.parameter.Option
import com.jetpackr.common.filter.VersionFilter
import io.ktor.client.request.get

val SDKMANLoader: SourceLoader = { client, url ->
    client.get<String>(url).split(",").filter(VersionFilter).sortedWith(VersionComparator).map {
        Option(value = it)
    }
}