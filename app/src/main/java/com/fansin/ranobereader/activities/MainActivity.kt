package com.fansin.ranobereader.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.SearchView
import android.view.View
import android.widget.LinearLayout
import com.fansin.ranobereader.data.Book
import com.fansin.ranobereader.adapters.BooksAdapter
import com.fansin.ranobereader.R
import com.fansin.ranobereader.RanobeApplication
import com.fansin.ranobereader.utils.BookManager
import com.fansin.ranobereader.utils.FileManager
import kotlinx.coroutines.*

//TODO: Menu with Login, Download Manager, Progress  circles, Menu for books to download

@Suppress("UNCHECKED_CAST")
class MainActivity : SearchActivity() {

    private var booksList : ArrayList<Book> = arrayListOf()
    private lateinit var booksAdapter: BooksAdapter
    private lateinit var myRecyclerView : RecyclerView

    init {
        onQueryTextListener = object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // do something on search text changed
                return true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myRecyclerView = findViewById(R.id.rv_books)
        myRecyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)

        booksAdapter = BooksAdapter(booksList, ::onShowBookDetails as ((Book) -> Unit))
        myRecyclerView.adapter = booksAdapter

        FileManager.initialize(applicationContext)

        loadBooks()
    }

    override fun onResume() {
        super.onResume()
        loadBooks()
    }

    private fun loadBooks() = (application as RanobeApplication).launch(Dispatchers.Default) {
        booksList = BookManager.getMyBooks() as ArrayList<Book>

        runOnUiThread {
            booksAdapter.updateItems(booksList)
        }
    }

    private fun onShowBookDetails(book: Book) {
        val intent = Intent(this, BookDetailsActivity::class.java)
        intent.putExtra("Book", book)
        startActivity(intent)
    }

    fun onFabClick(view: View) {
        val intent = Intent(this, AddBookActivity::class.java)
        startActivity(intent)
    }
}
