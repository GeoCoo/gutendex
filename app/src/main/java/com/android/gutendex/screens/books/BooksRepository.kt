package com.android.gutendex.screens.books

import com.android.gutendex.models.BookDomain
import com.android.gutendex.models.toBooksDomain
import com.android.gutendex.networking.ApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface BooksRepository {
    fun getBooks(page: Int): Flow<BooksRepositoryResponse>
}

class BooksRepositoryImpl @Inject constructor(private val apiClient: ApiClient) : BooksRepository {
    override fun getBooks(page: Int) = flow {
        val response = apiClient.retrieveBooksList(page)
        if (response.isSuccessful) {
            emit(
                BooksRepositoryResponse.Success(
                    response.body()?.toBooksDomain(),
                    response.body()?.count
                )
            )
        } else
            emit(BooksRepositoryResponse.Error(response.errorBody().toString()))
    }.catch {
        emit(BooksRepositoryResponse.Failed(errorMessage = "something went wrong"))
    }
}

sealed class BooksRepositoryResponse {
    data class Success(val booksList: List<BookDomain>?, val totalCount: Int?) :
        BooksRepositoryResponse()
    data class Error(val errorResponse: String) : BooksRepositoryResponse()
    data class Failed(val errorMessage: String) : BooksRepositoryResponse()
}