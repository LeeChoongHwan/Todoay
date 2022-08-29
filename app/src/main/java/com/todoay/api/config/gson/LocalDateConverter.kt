package com.todoay.api.config.gson

import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class LocalDateConverter {
    companion object {
        val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }

    class LocalDateSerializer : JsonSerializer<LocalDate> {
        override fun serialize(localDate: LocalDate?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            return JsonPrimitive(formatter.format(localDate))
        }
    }

    class LocalDateDeserializer : JsonDeserializer<LocalDate> {
        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDate {
            return LocalDate.parse(json?.asString, formatter.withLocale(Locale.ENGLISH))
        }

    }

}