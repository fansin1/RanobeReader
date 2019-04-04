package com.fansin.ranobereader.utils

import com.fansin.ranobereader.data.Book
import com.fansin.ranobereader.data.Chapter
import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONObject

object BookParser {
    private var gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()!!


    fun parse(jsonObject: JSONObject) : Book {

        val book = gson.fromJson(jsonObject.getJSONObject("book").toString(), Book::class.java)
        book.country = jsonObject.getJSONObject("book").getJSONObject("country").getString("title")

        var jsonArray = jsonObject.getJSONArray("genres")

        for (jo in 0 until jsonArray.length()) {
            book.genres.add(jsonArray.getJSONObject(jo).getString("title"))
        }

        jsonArray = jsonObject.getJSONArray("parts")

        for (jo in jsonArray.length() - 1 downTo 0) {
            book.chapters.add(gson.fromJson(jsonArray.getJSONObject(jo).toString(), Chapter::class.java))
        }

        book.imageUrl = jsonObject.getJSONObject("book").getJSONObject("image").getJSONObject("desktop").getString("image")

        return book

    }


    fun parseArray(jsonArray: JSONArray) : List<Book> { //TODO: Test it

        val bookArray = gson.fromJson(jsonArray.toString(), Array<Book>::class.java) ?: throw Exception()

        if (jsonArray.getJSONObject(0).has("label")) {
            for (i in 0 until jsonArray.length()) {
                val jsonBook = jsonArray.getJSONObject(i)
                bookArray[i].imageUrl = jsonBook.getString("image")
                bookArray[i].title = jsonBook.getString("label")
                bookArray[i].description = ""
                if (jsonBook.has("link")) {
                    bookArray[i].url = jsonBook.getString("link")
                }
            }
        } else {
            for (i in 0 until jsonArray.length()) {
                bookArray[i].imageUrl = jsonArray.getJSONObject(i).getJSONObject("image").getJSONObject("desktop").getString("image")
            }
        }

        return bookArray.toList()

    }
}