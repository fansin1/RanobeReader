package com.fansin.ranobereader.data

import android.arch.persistence.room.*
import android.content.Context
import com.fansin.ranobereader.converters.ChapterConverter
import com.fansin.ranobereader.converters.StringsConverter
import com.fansin.ranobereader.utils.AppDatabase
import com.fansin.ranobereader.utils.BookManager
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.Serializable

@Entity
class Book : Serializable{

    @Transient
    @Ignore
    var context: Context? = null

    @PrimaryKey
    @Expose
    var id: Int = 0

    @Expose
    lateinit var title: String
    @Expose
    lateinit var url: String
    @Expose
    lateinit var description: String
    var author = ""
    var country = ""

    @TypeConverters(StringsConverter::class)
    var genres: ArrayList<String> = arrayListOf()

    lateinit var imageUrl: String
    @Expose
    var likes: String = "0"
    @Expose
    var dislikes: String = "0"

    @SerializedName("view")
    @Expose
    var viewsCount: Int = 0

    var chaptersCount: Int = 0
        set(value) {
            field = value
            if (context != null)
                updateDB()
        }
    var chaptersDownloaded: Int = 0

    var progress: Int = 0
        set(value) {
            field = value
            updateDB()
        }

    @TypeConverters(ChapterConverter::class)
    var chapters: ArrayList<Chapter> = arrayListOf()

    private fun updateDB() {
        if (context != null)
            BookManager.updateBook(this)
    }

}