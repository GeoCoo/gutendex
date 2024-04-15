package com.android.gutendex.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<BookEntity>)

    @Query("SELECT * FROM books ORDER BY polularity DESC")
    suspend fun getAllBooks(): List<BookEntity>

    @Query("SELECT * FROM books LIMIT :pageSize OFFSET :offset")
    fun getBooks(pageSize: Int, offset: Int): Flow<List<BookEntity>>

    @Query("DELETE FROM books")
    suspend fun deleteAll()
}