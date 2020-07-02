package com.proficiency.assingment.db

import androidx.room.TypeConverter
import com.proficiency.assingment.model.FactsResponse

class Converters {
    @TypeConverter
    fun fromSource(source: FactsResponse.Source): String {
        return source.name
    }
    @TypeConverter
    fun toSource(name: String): FactsResponse.Source {
        return FactsResponse.Source(name, name)
    }
}