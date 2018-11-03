package com.jetpackr.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.jetpackr.common.data.deserializer.SelectDeserializer
import com.jetpackr.common.data.parameter.Select

fun yamlMapper(
        block: SimpleModule.() -> Unit = {}
): ObjectMapper {
    val objectMapper = ObjectMapper(YAMLFactory())
    val deserializerModule: SimpleModule = SimpleModule().apply(block)

    objectMapper.registerModule(deserializerModule)
    objectMapper.registerKotlinModule()

    return objectMapper
}

val mapper = yamlMapper {
    addDeserializer(Select::class.java, SelectDeserializer())
}