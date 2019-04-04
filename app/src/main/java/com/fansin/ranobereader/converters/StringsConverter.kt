package com.fansin.ranobereader.converters

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StringsConverter {

    var gson: Gson = Gson()

    @TypeConverter
    fun fromStrings(chapters: ArrayList<String>): String {
        return gson.toJson(chapters)
    }

    @TypeConverter
    fun toChapterList(value: String) : ArrayList<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }
}