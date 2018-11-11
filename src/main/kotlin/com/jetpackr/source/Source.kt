package com.jetpackr.source

import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

class Source(
        val type: Type,
        val url: String? = null
) {
    enum class Type {
        // Remote Source
        DockerHub,
        GitHub,
        NPMRegistry,
        SDKMAN,

        // Local Source
        TimeZone
    }

    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}