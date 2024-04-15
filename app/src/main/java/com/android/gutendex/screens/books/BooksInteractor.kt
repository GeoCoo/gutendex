package com.android.gutendex.screens.books

import com.android.gutendex.models.BookDomain
import com.android.gutendex.room.RoomBookRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface BooksInteractor {
    fun retrieveBooks(page: Int): Flow<BooksPartialState>
    fun retrieveNextBooks(page: Int): Flow<BooksPartialState>
    fun retrieveRoomBooks(): Flow<BooksPartialState>
    fun retrieveRoomBooksPaginated(pageSIze: Int, offset: Int): Flow<BooksPartialState>

    suspend fun saveBooksToDb(books: List<BookDomain>)
    suspend fun clearCache()
}

class BookInteractorImpl @Inject constructor(
    private val booksRepository: BooksRepository,
    private val roomBookRepository: RoomBookRepository,
    private val coroutineScope: CoroutineScope,
) : BooksInteractor {
    override fun retrieveBooks(page: Int): Flow<BooksPartialState> = flow {
        booksRepository.getBooks(page = page).collect {
            when (it) {
                is BooksRepositoryResponse.Failed -> {
                    emit(BooksPartialState.Failed(it.errorMessage))
                }

                is BooksRepositoryResponse.Success -> {
                    emit(BooksPartialState.Success(it.booksList, it.totalCount))
                }

                is BooksRepositoryResponse.Error -> {
                    emit(BooksPartialState.Failed(it.errorResponse))
                }
            }
        }
    }

    override fun retrieveNextBooks(page: Int): Flow<BooksPartialState> = flow {
        booksRepository.getBooks(page = page).collect {
            when (it) {
                is BooksRepositoryResponse.Failed -> {
                    emit(BooksPartialState.Failed(it.errorMessage))
                }

                is BooksRepositoryResponse.Success -> {
                    emit(BooksPartialState.Success(it.booksList, it.totalCount))
                }

                is BooksRepositoryResponse.Error -> {
                    emit(BooksPartialState.Failed(it.errorResponse))
                }
            }
        }
    }

    override fun retrieveRoomBooks(): Flow<BooksPartialState> = flow {
        roomBookRepository.getBooks().collect {
            when (it) {
                is BooksRepositoryResponse.Success -> {
                    emit(BooksPartialState.Success(it.booksList, it.totalCount))
                }

                is BooksRepositoryResponse.Error -> {
                    emit(BooksPartialState.Failed(it.errorResponse))
                }

                is BooksRepositoryResponse.Failed -> {
                    emit(BooksPartialState.Failed(it.errorMessage))
                }
            }
        }

    }

    override fun retrieveRoomBooksPaginated(pageSIze: Int, offset: Int): Flow<BooksPartialState> =
        flow {
            roomBookRepository.getBooksPaged(pageSIze, offset).collect {
                when (it) {
                    is BooksRepositoryResponse.Success -> {
                        emit(BooksPartialState.Success(it.booksList, it.totalCount))
                    }

                    is BooksRepositoryResponse.Error -> {
                        emit(BooksPartialState.Failed(it.errorResponse))
                    }

                    is BooksRepositoryResponse.Failed -> {
                        emit(BooksPartialState.Failed(it.errorMessage))
                    }
                }
            }
        }

    override suspend fun saveBooksToDb(books: List<BookDomain>) {
        coroutineScope.launch(Dispatchers.IO) {
            roomBookRepository.saveBooksToDb(books)
        }
    }

    override suspend fun clearCache() {
        roomBookRepository.clearBooks()
    }
}

sealed class BooksPartialState {
    data class Success(val books: List<BookDomain>?, val totalCount: Int?) : BooksPartialState()
    data class Failed(val errorMessage: String) : BooksPartialState()
}