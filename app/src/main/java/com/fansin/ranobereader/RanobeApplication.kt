package com.fansin.ranobereader

import android.app.Application
import com.fansin.ranobereader.utils.BookManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class RanobeApplication : Application(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate() {
        super.onCreate()
        BookManager.context = applicationContext
        job = Job()

    }

}