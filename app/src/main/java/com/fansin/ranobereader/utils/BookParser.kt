package com.fansin.ranobereader.utils

import com.fansin.ranobereader.data.Book
import com.fansin.ranobereader.data.Chapter
import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONObject

object BookParser {
    private var gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()!!


    fun parse(jsonObject: JSONObject) : Book {

        val book = gson.fromJson(jsonObject.toString(), Book::class.java)

        book.country = jsonObject
                .getJSONObject("country")
                .getString("title")

        var jsonArray = jsonObject.getJSONArray("genres")

        for (jo in 0 until jsonArray.length()) {
            book.genres.add(jsonArray.getJSONObject(jo).getString("title"))
        }

        jsonArray = jsonObject.getJSONArray("chapters")

        for (j1 in jsonArray.length() - 1 downTo 0) {
            book.chapters.add(gson.fromJson(jsonArray
                .getJSONObject(j1).toString(), Chapter::class.java))
        }

        book.imageUrl = jsonObject
                .getJSONArray("images")
                .getJSONObject(0)
                .getString("url")

        return book

    }


    fun parseArray(jsonArray: JSONArray) : List<Book> { //TODO: Test it

        val bookArray = gson.fromJson(jsonArray.toString(), Array<Book>::class.java) ?: throw Exception()

        for (i in 0 until jsonArray.length()) {
            val jsonBook = jsonArray.getJSONObject(i)

            bookArray[i].imageUrl = jsonBook
                    .getJSONArray("images")
                    .getJSONObject(0)
                    .getString("url")

            bookArray[i].title = jsonBook.getString("title")
            bookArray[i].description = jsonBook.getString("description")
            bookArray[i].url = jsonBook.getString("url")
        }

        return bookArray.toList()

    }
}