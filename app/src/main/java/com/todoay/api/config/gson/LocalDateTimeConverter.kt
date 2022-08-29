package com.todoay.api.config.gson

import com.google.gson.*
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class LocalDateTimeConverter {
    companion object {
        val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    }

    class LocalDateTimeSerializer : JsonSerializer<LocalDateTime> {
        override fun serialize(localdateTime: LocalDateTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            return JsonPrimitive(formatter.format(localdateTime))
        }
    }

    class LocalDateTimeDeserializer : JsonDeserializer<LocalDateTime> {
        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDateTime {
            return LocalDateTime.parse(json?.asString, formatter.withLocale(Locale.ENGLISH))
        }

    }
}