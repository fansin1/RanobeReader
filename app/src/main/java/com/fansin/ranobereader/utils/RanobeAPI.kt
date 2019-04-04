package com.fansin.ranobereader.utils

import com.fansin.ranobereader.data.Book
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject


object RanobeAPI {


    private val client = OkHttpClient()

    fun getText(bookUrl: String, chapterUrl: String) : String {

        val url = UrlBuilder.textUrl(bookUrl, chapterUrl)

        try {

            val jsonObject = JSONObject(client.newCall(Request.Builder().url(url).build()).
                    execute().body()!!.string()).
                    getJSONObject("result")

            return jsonObject.getJSONObject("part").getString("content")

        } catch (e: Exception) {
            throw e
        }

    }

    fun getBooks(limit: Int, offset: Int, order: String) : List<Book> { //TODO: change order to enum or some other type

        val url = UrlBuilder.allBooksUrl(limit, offset, order)

        try {

            val jsonArray = JSONObject(client.newCall(Request.Builder().url(url).build()).
                    execute().body()!!.string()).
                    getJSONObject("result").
                    getJSONArray("books")

            return BookParser.parseArray(jsonArray)

        } catch (e: Exception) {
            throw e
        }

    }


    fun getBook(bookUrl: String) : Book {

        val url = UrlBuilder.bookUrl(bookUrl)

        try {

            val jsonObject = JSONObject(client.newCall(Request.Builder().url(url).build()).
                    execute().body()!!.string()).
                    getJSONObject("result")

            return BookParser.parse(jsonObject)

        } catch (e: Exception) {
            throw e
        }

    }


    fun searchBooks(text: String) : List<Book> {

        val url = UrlBuilder.searchUrl(text)

        try {

            val jsonArray = JSONObject(client.newCall(Request.Builder().url(url).build()).
                    execute().body()!!.string()).
                    getJSONArray("result")

            return BookParser.parseArray(jsonArray)

        } catch (e: Exception) {
            throw e
        }

    }

    fun genresBooks(genres: Array<Int>) {
        //TODO someday (never)
    }

}