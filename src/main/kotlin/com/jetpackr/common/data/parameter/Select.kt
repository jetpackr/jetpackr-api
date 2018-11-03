package com.jetpackr.common.data.parameter

import com.jetpackr.common.data.source.Source
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

class Select(
        name: String,
        label: String,
        val options: List<Option>,
        val source: Source? = null
): Parameter(
        name,
        label
) {
    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}