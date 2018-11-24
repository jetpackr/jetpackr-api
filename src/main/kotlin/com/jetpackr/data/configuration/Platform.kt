package com.jetpackr.data.configuration

import com.jetpackr.data.Metadata
import com.jetpackr.data.parameter.Checkbox
import com.jetpackr.data.parameter.Select
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

open class Platform(
  name: String,
  label: String,
  description: String,
  alias: String? = null,
  val version: Select,
  val install: Checkbox? = null
): Metadata(
        name,
        label,
        description,
        alias
) {
    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}