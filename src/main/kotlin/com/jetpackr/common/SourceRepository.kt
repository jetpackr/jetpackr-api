package com.jetpackr.common

import com.jetpackr.common.data.parameter.Option
import com.jetpackr.common.data.source.Source
import com.jetpackr.common.data.source.Source.Type
import com.jetpackr.common.data.source.VagrantCloudResponse
import io.ktor.client.request.get
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.TimeZone

fun timeZone() =
        TimeZone.getAvailableIDs().mapNotNull { it ->
            if (!it.startsWith("SystemV"))
                Option(value = it)
            else
                null
        }

fun loadFromVagrantCloud(url: String) = GlobalScope.async {
    val instant = LocalDate.of(2015, 12, 31)
            .atStartOfDay()
            .atZone(ZoneId.systemDefault()).toInstant()

    val vagrantCloudResponse = client.get<VagrantCloudResponse>(url)

    vagrantCloudResponse.boxes.mapNotNull {
        val afterDate = Date.from(instant)

        if (it.username.equals("ubuntu", true)
                && it.tag.startsWith("ubuntu/", true)
                && it.name.endsWith("64")
                && it.createdAt.after(afterDate))
            Option(it.shortDescription, it.tag)
        else
            null
    }
}

fun loadOptionsFromSource(source: Source): List<Option> = runBlocking {
    when (source.type) {
        Source.Type.TimeZone -> timeZone()
        Source.Type.DockerHub -> listOf()
        Source.Type.VagrantCloud ->loadFromVagrantCloud(source.url as String).await()
        else -> throw Exception("'source' must be one of ${Type.values().joinToString { it.name }}")
    }
}
