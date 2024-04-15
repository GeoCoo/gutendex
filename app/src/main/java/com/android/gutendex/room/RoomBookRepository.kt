package com.android.gutendex.room

import com.android.gutendex.models.BookDomain
import com.android.gutendex.models.toBooksDomain
import com.android.gutendex.screens.books.BooksRepositoryResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject


interface RoomBookRepository {
    fun getBooks(): Flow<BooksRepositoryResponse>
    suspend fun getBooksPaged(pageSize: Int, offset: Int): Flow<BooksRepositoryResponse>
    suspend fun saveBooksToDb(books: List<BookDomain>)
    suspend fun clearBooks()
}

class RoomBookRepositoryImpl @Inject constructor(
    private val bookDao: BookDao,
    private var coroutineScope: CoroutineScope
) :
    RoomBookRepository {
    override fun getBooks(): Flow<BooksRepositoryResponse> = flow {
        val books = bookDao.getAllBooks()
        if (books.isEmpty().not()) {
            emit(BooksRepositoryResponse.Success(books.toBooksDomain(), books.size))
        } else {
            emit(BooksRepositoryResponse.Error("no books in db"))
        }
    }

    override suspend fun getBooksPaged(pageSize: Int, offset: Int): Flow<BooksRepositoryResponse> =
        flow {
            bookDao.getBooks(pageSize = pageSize, offset = offset).collect {
                if (it.isEmpty().not()) {
                    emit(BooksRepositoryResponse.Success(it.toBooksDomain(), it.size))
                } else {
                    emit(BooksRepositoryResponse.Error("no books in db"))
                }
            }
        }

    override suspend fun saveBooksToDb(books: List<BookDomain>) {
        val dbBooks = books.map {
            BookEntity(
                it.id,
                title = it.title ?: "",
                author = it.author ?: "",
                popularity = it.popularity,
                image = it.img
            )
        }
        coroutineScope.launch(Dispatchers.IO) {
            bookDao.insertBooks(dbBooks)
        }
    }

    override suspend fun clearBooks() {
        bookDao.deleteAll()
    }
}