package com.android.gutendex.screens.books

import com.android.gutendex.models.BookListing
import com.android.gutendex.models.toListOfBooks
import com.android.gutendex.networking.ApiClient
import com.android.gutendex.networking.BooksResponseDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface BooksRepository {
    fun getBooks(): Flow<BooksRepositoryResponse>
}

class BooksRepositoryImpl @Inject constructor(private val apiClient: ApiClient) : BooksRepository {
    override fun getBooks() = flow {
        val response = apiClient.retrieveBooksList()
        if (response.isSuccessful) {
            emit(BooksRepositoryResponse.Success(response.body()?.toListOfBooks()))
        } else
            emit(BooksRepositoryResponse.Error(response.errorBody().toString()))
    }.catch {
        emit(BooksRepositoryResponse.Failed(errorMessage = "something went wrong"))
    }

}

sealed class BooksRepositoryResponse {
    data class Success(val booksList: List<BookListing>?) : BooksRepositoryResponse()
    data class Error(val errorResponse: String) : BooksRepositoryResponse()
    data class Failed(val errorMessage: String) : BooksRepositoryResponse()
}