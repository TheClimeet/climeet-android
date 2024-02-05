package com.climus.climeet.presentation.ui.main.record.timer.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataListConverters {
    private val gson = Gson()

    @TypeConverter
    fun jsonToList(value: String): List<ClimbingRecord> {
        val listType = object : TypeToken<List<ClimbingRecord>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun listToJson(list: List<ClimbingRecord>): String {
        return gson.toJson(list)
    }
}