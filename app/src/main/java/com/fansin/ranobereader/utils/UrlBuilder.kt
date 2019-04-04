package com.fansin.ranobereader.utils

import android.net.Uri

object UrlBuilder {

    private const val home = "xn--80ac9aeh6f.xn--p1ai"


    fun textUrl(bookUrl: String, chapterUrl: String): String {
        val bookName = if (bookUrl[0] == '/')
            bookUrl.substring(1, bookUrl.length - 1) else
            bookUrl.substring(bookUrl.indexOf("/", 9, true) + 1, bookUrl.length - 1)

        var chapterName = chapterUrl.substring(0, chapterUrl.length - 1)
        chapterName = chapterName.substring(chapterName.lastIndexOf("/") + 1)

        return Uri.Builder().scheme("https").authority(home).appendPath("v1").appendPath("part").appendPath("get").appendPath("").appendQueryParameter("bookAlias", bookName).appendQueryParameter("partAlias", chapterName).build().toString()
    }

    fun allBooksUrl(limit: Int, offset: Int, order: String) = Uri.Builder().scheme("https").authority(home).appendPath("v1").appendPath("book").appendPath("list").appendPath("").appendQueryParameter("country", "").appendQueryParameter("limit", limit.toString()).appendQueryParameter("offset", offset.toString()).appendQueryParameter("order", order).build().toString()

    fun bookUrl(bookUrl: String): String {
        val name = if (bookUrl[0] == '/')
            bookUrl.substring(1, bookUrl.length - 1) else
            bookUrl.substring(bookUrl.indexOf("/", 9, true) + 1, bookUrl.length - 1)

        return Uri.Builder().scheme("https").authority(home).appendPath("v1").appendPath("book").appendPath("get").appendPath("").appendQueryParameter("bookAlias", name).build().toString()
    }

    fun searchUrl(text: String) : String {
        return  Uri.Builder().
                scheme("https").
                authority(home).
                appendPath("v1").
                appendPath("book").
                appendPath("search").
                appendPath("").
                appendQueryParameter("q", text).build().toString()
    }

}