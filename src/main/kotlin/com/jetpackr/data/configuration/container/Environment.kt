package com.jetpackr.data.configuration.container

import com.jetpackr.data.parameter.Text
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

class Environment(
        val defaults: Map<String, String>? = null,
        val others: List<Text>? = null
) {
    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}