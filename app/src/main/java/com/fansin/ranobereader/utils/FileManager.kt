package com.fansin.ranobereader.utils

import android.annotation.SuppressLint
import android.content.Context
import com.fansin.ranobereader.data.Book
import com.fansin.ranobereader.data.Chapter
import java.io.File

@SuppressLint("StaticFieldLeak")
object FileManager {

    lateinit var context: Context

    private fun isChapterDownloaded(chapter: Chapter): Boolean {
        return File(context.filesDir, chapter.url.filterNot { it == '/' }).exists()
    }

    fun getText(chapter: Chapter): Pair<String, Boolean> {
        return if (isChapterDownloaded(chapter)) {
            lateinit var s: String
            context.openFileInput(chapter.url.filterNot { it == '/' }).bufferedReader().use {
                s = it.readText()
            }
            Pair(s, true)
        } else {
            Pair("", false)
        }
    }

    fun saveText(book: Book, chapter: Chapter) {

    }

    fun initialize(context: Context) {
        this.context = context
    }
}