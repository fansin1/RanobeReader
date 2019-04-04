package com.fansin.ranobereader.utils

import android.app.Activity
import android.content.Context
import android.widget.TextView
import com.fansin.ranobereader.R
import com.fansin.ranobereader.data.Book
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.lang.Exception

object DownloadManager {

    var bookHashMap: HashMap<Int, Activity> = hashMapOf()

    fun addOrUpdate(book: Book, activity: Activity) {
        bookHashMap[book.id] = activity
    }

    fun downloadBook(coroutineScope: CoroutineScope, context: Context, book: Book) =
            coroutineScope.launch(Dispatchers.Default) {
        for ((i, chapter) in book.chapters.withIndex()) {
            val s = RanobeAPI.getText(book.url, chapter.url)
            try {
                val file = File(context.filesDir, chapter.url.filterNot { it == '/' })
                if (!file.exists()) {
                    file.createNewFile()
                }
                val output = BufferedWriter(FileWriter(file))
                output.write(s)
                output.close()
            } catch (e: Exception) {
                book.chaptersDownloaded = i
            }
            if (bookHashMap.containsKey(book.id)) {
                bookHashMap[book.id]?.runOnUiThread {
                    val progressTv = bookHashMap[book.id]!!.findViewById<TextView>(R.id.progress_tv)
                    if (progressTv != null)
                        progressTv.text = i.toString() + "/" + book.chapters.size.toString()
                }
            }
        }
    }
}