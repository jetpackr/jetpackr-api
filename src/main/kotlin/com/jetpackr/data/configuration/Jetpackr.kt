package com.jetpackr.data.configuration

import com.jetpackr.data.configuration.container.Container
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

class Jetpackr(
        val machine: Machine,
        val kits: Map<String, Tool>,
        val containers: Map<String, Container>
) {
    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE)
    }
}