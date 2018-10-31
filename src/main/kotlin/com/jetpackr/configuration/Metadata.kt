package com.jetpackr.configuration

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

@JsonIgnoreProperties(ignoreUnknown = true)
open class Metadata(
        val name: String,
        val alias: String? = null,
        val label: String,
        val description: String? = null
) {
    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}