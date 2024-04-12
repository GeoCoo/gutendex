package com.android.gutendex.screens.books

import android.widget.Toast
import androidx.core.content.contentValuesOf
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.android.gutendex.Application
import com.android.gutendex.base.MviViewModel
import com.android.gutendex.base.ViewEvent
import com.android.gutendex.base.ViewSideEffect
import com.android.gutendex.base.ViewState
import com.android.gutendex.helpers.ResourceProvider
import com.android.gutendex.models.BookListing
import com.android.gutendex.models.NavRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class State(
    val isLoading: Boolean = false,
    val listOfBooks: List<BookListing>? = listOf(),
    val errorMessage: String? = null


) : ViewState

sealed class Event : ViewEvent {
    data object GetBooks : Event()
    data class NavigateToInfo(val id: Int, val navController: NavController) : Event()
}

sealed class Effect : ViewSideEffect {

}

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val booksInteractor: BooksInteractor,
    private val resourceProvider: ResourceProvider

) :
    MviViewModel<Event, State, Effect>() {


    override fun setInitialState(): State =
        State(
        )

    override fun handleEvents(event: Event) {
        when (event) {
            is Event.GetBooks -> {
                setState { State(isLoading = true) }
                viewModelScope.launch {
                    booksInteractor.retrieveBooks().collect {
                        when (it) {
                            is BooksPartialState.Success -> {
                                setState { copy(listOfBooks = it.books, isLoading = false) }

                            }

                            is BooksPartialState.Failed -> {
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

            is Event.NavigateToInfo -> {
                val route = NavRoutes.BOOK.route.replace("{id}", event.id.toString())
                event.navController.navigate("/book/${event.id}")
            }
        }

    }
}