package com.android.gutendex.screens.books

import com.android.gutendex.models.BookListing
import com.android.gutendex.models.toListOfBooks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface BooksInteractor {
    fun retrieveBooks(): Flow<BooksPartialState>
}

class BookInteractorImpl @Inject constructor(
    private val booksRepository: BooksRepository
) : BooksInteractor {
    override fun retrieveBooks(): Flow<BooksPartialState> = flow {
        booksRepository.getBooks().collect {
            when (it) {
                is BooksRepositoryResponse.Failed -> {
                    emit(BooksPartialState.Failed(it.errorMessage))
                }

                is BooksRepositoryResponse.Success -> {
                    emit(BooksPartialState.Success(it.booksList))
                }

                is BooksRepositoryResponse.Error -> {
                    emit(BooksPartialState.Failed(it.errorResponse))

                }
            }
        }
    }

}

sealed class BooksPartialState {
    data class Success(val books: List<BookListing>?) : BooksPartialState()
    data class Failed(val errorMessage: String) : BooksPartialState()
}