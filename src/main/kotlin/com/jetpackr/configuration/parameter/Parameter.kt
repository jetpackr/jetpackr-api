package com.jetpackr.configuration.parameter

import com.jetpackr.configuration.Metadata
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

open class Parameter(
        name: String,
        label: String
): Metadata(
        name,
        label
) {
    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}