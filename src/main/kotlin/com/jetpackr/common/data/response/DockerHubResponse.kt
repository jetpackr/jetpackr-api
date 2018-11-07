package com.jetpackr.common.data.response

import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

class DockerHubResponse(
        val next: String? = null,
        val results: List<Map<String, Any>>
) {
    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}