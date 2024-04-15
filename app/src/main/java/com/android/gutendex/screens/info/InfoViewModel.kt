package com.android.gutendex.screens.info


import androidx.lifecycle.viewModelScope
import com.android.gutendex.base.MviViewModel
import com.android.gutendex.base.ViewEvent
import com.android.gutendex.base.ViewSideEffect
import com.android.gutendex.base.ViewState
import com.android.gutendex.models.BookInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class State(
    val isLoading: Boolean = false,
    val bookInfo: BookInfo?= null,
    val errorMessage: String? = ""


) : ViewState

sealed class Event : ViewEvent {
    data class GetBookInfo(val bookId: String) : Event()
}

sealed class Effect : ViewSideEffect {

}

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val infoInteractor: InfoInteractor
) :
    MviViewModel<Event, State, Effect>() {


    override fun setInitialState(): State =
        State(
            isLoading = true,
        )

    override fun handleEvents(event: Event) {
        when (event) {
            is Event.GetBookInfo -> {
                viewModelScope.launch {
                    infoInteractor.retrieveBookInfo(event.bookId).collect {
                        when (it) {
                            is BookInfoPartialState.Success -> {
                                setState {
                                    copy(
                                        bookInfo = it.bookInfo,
                                        isLoading = false
                                    )
                                }
                            }

                            is BookInfoPartialState.Failed -> {
                                setState {
                                    copy(
                                        errorMessage = it.errorMessage,
                                        isLoading = false
                                    )
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}