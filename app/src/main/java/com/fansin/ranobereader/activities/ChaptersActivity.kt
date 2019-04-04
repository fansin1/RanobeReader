package com.fansin.ranobereader.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import com.fansin.ranobereader.data.Book
import com.fansin.ranobereader.data.Chapter
import com.fansin.ranobereader.adapters.ChaptersAdapter
import com.fansin.ranobereader.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class ChaptersActivity : AppCompatActivity() {

    private lateinit var detailsAdapter: ChaptersAdapter
    private lateinit var myRecyclerView : RecyclerView
    private lateinit var book : Book

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapters)

        myRecyclerView = findViewById(R.id.rv_details)
        myRecyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        book = intent.getSerializableExtra("Book") as Book
        detailsAdapter = ChaptersAdapter(book.chapters, ::onChapterClick as ((Chapter, Int) -> Unit))
        myRecyclerView.adapter = detailsAdapter
    }

    private fun onChapterClick(chapter: Chapter, chapterNumber: Int) {
        val intent = Intent(this, ChapterActivity::class.java)
        book.context = applicationContext
        book.progress = chapterNumber
        intent.putExtra("Book", book)
        intent.putExtra("Chapter", chapterNumber)
        startActivity(intent)
    }
}
