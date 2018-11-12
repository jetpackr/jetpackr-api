package com.jetpackr.source

import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

class Source(
        val type: Type,
        val url: String
) {
    enum class Type {
        DockerHub,
        GitHub,
        NPMRegistry,
        SDKMAN
    }

    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}