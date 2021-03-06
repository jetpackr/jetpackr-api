package com.jetpackr.data.parameter

import com.jetpackr.data.Metadata
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

open class Parameter(
        name: String,
        label: String,
        val span: Boolean
): Metadata(
        name,
        label
) {
    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}