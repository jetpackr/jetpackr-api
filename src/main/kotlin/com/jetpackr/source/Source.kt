package com.jetpackr.source

import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

class Source(
        val local: Local = Local.None,
        val remote: Remote = Remote.None,
        val url: String = "https://"
) {
    enum class Local {
        Timezone,
        None
    }

    enum class Remote {
        DockerHub,
        GitHub,
        NPMRegistry,
        SDKMAN,
        None
    }

    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}