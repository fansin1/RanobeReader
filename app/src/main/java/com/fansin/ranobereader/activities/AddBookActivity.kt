package com.fansin.ranobereader.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.SearchView
import com.fansin.ranobereader.*
import com.fansin.ranobereader.adapters.BooksAdapter
import com.fansin.ranobereader.data.Book
import com.fansin.ranobereader.utils.BookManager
import com.fansin.ranobereader.utils.RanobeAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

@Suppress("UNCHECKED_CAST")
class AddBookActivity: SearchActivity(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var booksList : ArrayList<Book> = arrayListOf()
    private lateinit var booksAdapter: BooksAdapter
    private lateinit var myRecyclerView : RecyclerView
    private lateinit var scrollListener: OnScrollListener
    private lateinit var swipeContainer : SwipeRefreshLayout
    private lateinit var mHandler: Handler

    init {
        onQueryTextListener = object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                launch (Dispatchers.Default){
                    booksList.clear()
                    try {
                        booksList.addAll(BookManager.searchBooks(query!!))
                    } catch (e: Exception) {}

                    runOnUiThread {
                        booksAdapter.updateItems(booksList)
                    }
                }
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
        setContentView(R.layout.activity_add_book)

        job = Job()
        mHandler = Handler()

        booksAdapter = BooksAdapter(booksList, ::bookClick as ((Book) -> Unit))

        myRecyclerView = findViewById(R.id.rv_add_books)
        myRecyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        myRecyclerView.adapter = booksAdapter
        scrollListener = OnScrollListener(myRecyclerView.layoutManager as LinearLayoutManager, booksAdapter, booksList, this)
        myRecyclerView.addOnScrollListener(scrollListener)

        swipeContainer = findViewById(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener {
            updateList()
        }
        updateList()

    }

    private fun updateList() {
        swipeContainer.isRefreshing = true
        launch(Dispatchers.Default) {
            booksList.clear()
            scrollListener.restore()
            try {
                booksList.addAll(BookManager.getBooks(12, 0, "popular"))
            } catch (e: Exception) {
                throw e
            }

            runOnUiThread {
                booksAdapter.updateItems(booksList)
                swipeContainer.isRefreshing = false
            }
        }
    }

    private fun bookClick(book: Book) {
        val intent = Intent(this, BookDetailsActivity::class.java)
        intent.putExtra("Book", book.url)
        startActivity(intent)
    }
}
