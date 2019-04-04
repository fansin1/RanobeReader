package com.fansin.ranobereader.converters

import android.arch.persistence.room.TypeConverter
import com.fansin.ranobereader.data.Chapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ChapterConverter {

    var gson: Gson = Gson()

    @TypeConverter
    fun fromChapter(chapters: ArrayList<Chapter>): String {
        return gson.toJson(chapters)
    }

    @TypeConverter
    fun toChapterList(value: String) : ArrayList<Chapter> {
        val listType = object : TypeToken<List<Chapter>>() {}.type
        return gson.fromJson(value, listType)
    }
}