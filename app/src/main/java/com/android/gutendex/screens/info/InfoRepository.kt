package com.android.gutendex.screens.info

import com.android.gutendex.models.BookInfo
import com.android.gutendex.models.toBookInfoDomain
import com.android.gutendex.networking.ApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface InfoRepository {
    fun getBookInfo(id: String): Flow<BookInfoRepositoryResponse>
}

class InfoRepositoryImpl @Inject constructor(private val apiClient: ApiClient) : InfoRepository {
    override fun getBookInfo(id: String) = flow {
        val response = apiClient.retrieveBookById(id)
        if (response.isSuccessful) {
            emit(BookInfoRepositoryResponse.Success(response.body()?.toBookInfoDomain()))
        } else
            emit(BookInfoRepositoryResponse.Error(response.errorBody().toString()))
    }.catch {
        emit(BookInfoRepositoryResponse.Failed(it.localizedMessage.toString()))
    }
}

sealed class BookInfoRepositoryResponse {
    data class Success(val bookInfo: BookInfo?) : BookInfoRepositoryResponse()
    data class Error(val errorResponse: String) : BookInfoRepositoryResponse()
    data class Failed(val errorMessage: String) : BookInfoRepositoryResponse()
}