package com.jetpackr.configuration

import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

class Repository(
        val type: Type,
        val url: String
) {
    enum class Type {
        DockerHub,
        GitHub,
        NPMRegistry,
        SDKMAN,
        VagrantCloud,
        None
    }

    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}