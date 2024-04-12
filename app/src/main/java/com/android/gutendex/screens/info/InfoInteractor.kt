package com.android.gutendex.screens.info

import com.android.gutendex.models.BookInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface InfoInteractor {
    fun retrieveBookInfo(id: String): Flow<BookInfoPartialState>
}

class InfoInteractorImpl @Inject constructor(private val infoRepository: InfoRepository) :
    InfoInteractor {
    override fun retrieveBookInfo(id: String): Flow<BookInfoPartialState> = flow {
        infoRepository.getBookInfo(id).collect {
            when (it) {
                is BookInfoRepositoryResponse.Success -> {
                    emit(BookInfoPartialState.Success(it.bookInfo))
                }
                is BookInfoRepositoryResponse.Failed->{
                    emit(BookInfoPartialState.Failed(it.errorMessage))
                }
                is BookInfoRepositoryResponse.Error->{
                    emit(BookInfoPartialState.Failed(it.errorResponse))
                }
            }
        }
    }

}

sealed class BookInfoPartialState {
    data class Success(val bookInfo: BookInfo?) : BookInfoPartialState()
    data class Failed(val errorMessage: String) : BookInfoPartialState()
}