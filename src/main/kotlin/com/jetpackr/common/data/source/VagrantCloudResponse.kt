package com.jetpackr.common.data.source

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.jetpackr.common.data.deserializer.DateDeserializer
import java.util.Date

data class VagrantCloudResponse(
        val boxes: List<Box>
) {
    class Box(
        @JsonProperty("username")
        val username: String,

        @JsonProperty("name")
        val name: String,

        @JsonProperty("short_description")
        val shortDescription: String,

        @JsonProperty("tag")
        val tag: String,

        @JsonProperty("created_at")
        @JsonDeserialize(using = DateDeserializer::class)
        val createdAt: Date
    )
}