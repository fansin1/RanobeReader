package com.fansin.ranobereader.utils

import android.annotation.SuppressLint
import android.content.Context
import com.fansin.ranobereader.data.Book
import com.fansin.ranobereader.data.Chapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

@SuppressLint("StaticFieldLeak")
object BookManager{

    var context: Context? = null

    //Get favourite books or null array if haven't them

    private fun addContext(bookList: List<Book>) {
        bookList.forEach {
            it.context = context
        }
    }

    fun getText(book: Book, chapter: Chapter): Pair<String, Boolean> {
        val file = FileManager.getText(chapter)
        return if (file.second) {
            file
        } else {
            lateinit var s: String
            try {
                s = RanobeAPI.getText(book.url, chapter.url)
                Pair(s, true)
            } catch (e: Exception) {
                Pair("", false)
            }
        }

    }

    fun getMyBooks(): List<Book> {
        val booksList = AppDatabase.getDatabase(context!!)?.bookDao()?.getAll()

        if (booksList != null)
            addContext(booksList)

        return booksList ?: listOf()
    }

    fun getBook(url: String): Book {
        var book = AppDatabase.getDatabase(context!!)?.bookDao()?.getByUrl(url)

        if (book == null)
            book = RanobeAPI.getBook(url)

        book.context = context

        return book
    }

    fun getBooks(limit: Int, offset: Int, order: String): List<Book> {
        try {
            val booksList = RanobeAPI.getBooks(limit, offset, order)

            addContext(booksList)

            return booksList
        } catch (e: Exception) {
            throw e
        }
    }

    fun addToMy(book: Book) {
        try {
            (context!! as CoroutineScope).launch(Dispatchers.Default) {
                AppDatabase.getDatabase(context!!)?.bookDao()?.insert(book)
            }
        } catch(e: Exception) {
            throw e
        }
    }

    fun deleteFromMy(book: Book) {
        try {
            (context!! as CoroutineScope).launch(Dispatchers.Default) {
                AppDatabase.getDatabase(context!!)?.bookDao()?.delete(book)
            }
        } catch(e: Exception) {
            throw e
        }
    }

    fun searchBooks(text: String): List<Book> {
        try {
            val booksList = RanobeAPI.searchBooks(text)

            addContext(booksList)

            return booksList
        } catch (e: Exception) {
            throw e
        }
    }

    fun isMine(book: Book): Boolean {
        return AppDatabase.getDatabase(context!!)?.bookDao()?.getById(book.id) != null
    }

    fun getUpdatedBook(book: Book): Book {
        return AppDatabase.getDatabase(context!!)?.bookDao()?.getById(book.id)!!
    }

    fun updateBook(book: Book) = (context!! as CoroutineScope).launch(Dispatchers.Default) {
        AppDatabase.getDatabase(context!!)?.bookDao()?.update(book)
    }
}