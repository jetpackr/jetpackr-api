package com.jetpackr.common.data.source

import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

class Source(
        val type: Type,
        val url: String? = null
) {
    enum class Type {
        // 3rd party APIs
        DockerHub,
        GitHub,
        NPMRegistry,
        SDKMAN,
        VagrantCloud,

        TimeZone
    }

    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}