package com.fansin.ranobereader.activities

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import com.fansin.ranobereader.data.Book
import com.fansin.ranobereader.R
import com.fansin.ranobereader.RanobeApplication
import com.fansin.ranobereader.data.Chapter
import com.fansin.ranobereader.utils.BookManager
import com.fansin.ranobereader.utils.RanobeAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class ChapterActivity : AppCompatActivity() {

    private lateinit var chapter: Chapter
    private var chapterNumber: Int = 0
    lateinit var book: Book
    private lateinit var scrollView: ScrollView
    private lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var nextBtn: Button
    private lateinit var prevBtn: Button
    private lateinit var chapterText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter)

        scrollView = findViewById(R.id.chapter_scroll)
        swipeContainer = findViewById(R.id.swipe_container)
        nextBtn = findViewById(R.id.next_btn)
        prevBtn = findViewById(R.id.prev_btn)
        chapterText = findViewById(R.id.chapter_text)

        swipeContainer.setOnRefreshListener {
            loadChapter()
        }


        book = intent.getSerializableExtra("Book") as Book
        chapterNumber = intent.getSerializableExtra("Chapter") as Int
        chapter = book.chapters[chapterNumber]
        book.context = applicationContext
        loadChapter()
    }

    private fun loadChapter() {
        prevBtn.visibility = View.GONE
        nextBtn.visibility = View.GONE
        swipeContainer.isRefreshing = true

        (application as RanobeApplication).launch (Dispatchers.Default) {

            if (chapterNumber < 0 || !::book.isInitialized || chapterNumber >= book.chapters.size) {
                return@launch
            }

            val chapterPair = BookManager.getText(book, chapter)

            if (chapterPair.second) {
                book.progress = chapterNumber
                runOnUiThread {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        chapterText.text = Html.fromHtml(chapterPair.first, Html.FROM_HTML_MODE_LEGACY)
                    } else {
                        chapterText.text = Html.fromHtml(chapterPair.first)
                    }
                    prevBtn.isEnabled = chapterNumber != 0
                    nextBtn.isEnabled = chapterNumber != book.chapters.size - 1
                }
            } else {
                chapterText.text = "Error while loading chapter"
            }
            runOnUiThread {
                prevBtn.visibility = View.VISIBLE
                nextBtn.visibility = View.VISIBLE
                swipeContainer.isRefreshing = false
                scrollView.smoothScrollTo(0, 0)
            }
        }
    }

    fun onNextClick(v: View) {
        chapter = book.chapters[++chapterNumber]
        loadChapter()
    }

    fun onPreviousClick(v: View) {
        chapter = book.chapters[++chapterNumber]
        loadChapter()
    }
}
