package com.villamaster.ai.data.database.converter

import androidx.room.TypeConverter
import java.util.Date

/**
 * Convertisseur de types pour Room Database
 * Convertit les dates en timestamps et vice versa
 */
class DateConverter {
    
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }
    
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}