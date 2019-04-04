package com.fansin.ranobereader.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.View
import android.widget.*
import com.fansin.ranobereader.data.Book
import com.fansin.ranobereader.R
import com.fansin.ranobereader.RanobeApplication
import com.fansin.ranobereader.utils.BookManager
import com.fansin.ranobereader.utils.DownloadManager
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class BookDetailsActivity: AppCompatActivity() {

    lateinit var book: Book
    private var downloaded: Boolean = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)

        val intentData = intent.getSerializableExtra("Book")

        (application as RanobeApplication).launch(Dispatchers.Default) {

            downloaded = false
            var loads = 0

            while (!downloaded && loads < 10) {
                try {
                    book = if (intentData is Book) {
                        intentData
                    } else {
                        BookManager.getBook(intentData as String)
                    }
                    downloaded = true
                } catch (e: Exception) {
                    loads++
                }
            }

            if (downloaded) {
                val isMine = BookManager.isMine(book)
                runOnUiThread {
                    if (isMine) {
                        findViewById<ImageButton>(R.id.add_or_delete).setImageResource(R.drawable.ic_delete_book)
                    }

                    Picasso.get().load(book.imageUrl).into(findViewById<ImageView>(R.id.thumbnail))

                    findViewById<TextView>(R.id.tv_title).text = book.title
                    findViewById<TextView>(R.id.tv_author).text = "Author: ${book.author}"
                    findViewById<TextView>(R.id.tv_country).text = "Country: ${book.country}"
                    findViewById<TextView>(R.id.tv_rating).text = "Likes: ${book.likes}/ Dislikes: ${book.dislikes}"

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        findViewById<TextView>(R.id.tv_description).text = Html.fromHtml(book.description, Html.FROM_HTML_MODE_LEGACY)
                    } else {
                        findViewById<TextView>(R.id.tv_description).text = Html.fromHtml(book.description)
                    }

                    var genres = "Genres: "
                    for (i in book.genres) {
                        genres += "$i "
                    }

                    findViewById<TextView>(R.id.tv_genres).text = genres

                    findViewById<ScrollView>(R.id.scrollView).smoothScrollTo(0, 0)

                    DownloadManager.addOrUpdate(book, this@BookDetailsActivity)
                }
            } else {
                runOnUiThread {
                    Toast.makeText(applicationContext, "ERROR: Try to reopen book", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        (application as RanobeApplication).launch (Dispatchers.Default) {
            if (::book.isInitialized && BookManager.isMine(book))
                book = BookManager.getUpdatedBook(book)
        }
    }

    fun onChaptersClick(v: View) {
        val intent = Intent(this, ChaptersActivity::class.java)
        intent.putExtra("Book", book)
        startActivity(intent)
    }

    fun onReadClick(v: View) {
        val intent = Intent(this, ChapterActivity::class.java)
        intent.putExtra("Book", book)
        intent.putExtra("Chapter", 0)
        startActivity(intent)
    }

    fun onContinueClick(v: View) {
        val intent = Intent(this, ChapterActivity::class.java)
        intent.putExtra("Book", book)
        intent.putExtra("Chapter", book.progress)
        startActivity(intent)
    }

    fun onAddOrDelete(v: View) {
        if (downloaded) {
            (application as RanobeApplication).launch(Dispatchers.Default) {
                val isMine = BookManager.isMine(book)
                runOnUiThread {
                    if (isMine) {
                        BookManager.deleteFromMy(book)
                        findViewById<ImageButton>(R.id.add_or_delete).setImageResource(R.drawable.ic_add_to_my)
                    } else {
                        BookManager.addToMy(book)
                        findViewById<ImageButton>(R.id.add_or_delete).setImageResource(R.drawable.ic_delete_book)
                    }
                }
            }
        }
    }

    fun onDownloadClick(v: View) {
        DownloadManager.downloadBook((application as RanobeApplication), applicationContext, book)
    }
}