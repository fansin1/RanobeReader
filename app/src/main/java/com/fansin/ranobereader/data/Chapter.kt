package com.fansin.ranobereader.data

import com.google.gson.annotations.Expose
import java.io.Serializable

class Chapter : Serializable {
    @Expose
    lateinit var title: String
    @Expose
    var id: Int = 0
    var number: Int = 0
    @Expose
    var url: String = ""
    var status: ChapterStatus = ChapterStatus.NOTDOWNLOADED
    var read: Boolean = false
    var progress: Int = 0
}