package com.jetpackr.common.data.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

class DateDeserializer(
        private val format: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
): JsonDeserializer<Date>() {
    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(parser: JsonParser,
                             context: DeserializationContext): Date {
        try {
            return format.parse(parser.text)
        } catch (e: ParseException) {
            throw RuntimeException(e)
        }
    }
}