package com.fansin.ranobereader.utils

import android.arch.persistence.room.*
import com.fansin.ranobereader.data.Book

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getAll(): List<Book>

    @Query("SELECT * FROM book WHERE id = :id")
    fun getById(id: Int): Book?

    @Query("SELECT * FROM book WHERE url = :url")
    fun getByUrl(url: String): Book?

    @Insert
    fun insert(book: Book)

    @Delete
    fun delete(book: Book)

    @Update
    fun update(book: Book)
}