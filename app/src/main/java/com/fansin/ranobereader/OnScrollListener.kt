package com.fansin.ranobereader

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.fansin.ranobereader.activities.AddBookActivity
import com.fansin.ranobereader.adapters.BooksAdapter
import com.fansin.ranobereader.data.Book
import com.fansin.ranobereader.utils.RanobeAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


class OnScrollListener(private val layoutManager: LinearLayoutManager, private val adapter: BooksAdapter,
                       private val dataList: ArrayList<Book>, val context: AddBookActivity) : RecyclerView.OnScrollListener() {
    private var previousTotal = 0
    private var loading = true
    private val visibleThreshold = 12
    private var firstVisibleItem = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0

    fun restore() {
        previousTotal = 0
        loading = true
        firstVisibleItem = 0
        visibleItemCount = 0
        totalItemCount = 0
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        visibleItemCount = recyclerView.childCount
        totalItemCount = layoutManager.itemCount
        firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }

        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            loading = true
            context.launch (Dispatchers.Default) {
                try {
                    dataList.addAll(RanobeAPI.getBooks(12, previousTotal, "popular"))
                } catch (e: Exception) {
                    loading = false
                }

                for (book in dataList) {
                    book.context = context
                }

                context.runOnUiThread {
                    adapter.updateItems(dataList)
                }
            }
            //val updatedSize = dataList.size
            //recyclerView.post { adapter.notifyItemRangeInserted(initialSize, updatedSize) }
        }
    }
}