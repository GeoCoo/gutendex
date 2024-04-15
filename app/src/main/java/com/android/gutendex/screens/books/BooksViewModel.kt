package com.android.gutendex.screens.books

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.android.gutendex.base.MviViewModel
import com.android.gutendex.base.ViewEvent
import com.android.gutendex.base.ViewSideEffect
import com.android.gutendex.base.ViewState
import com.android.gutendex.models.BookDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

data class State(
    val listOfBooks: List<BookDomain> = listOf(),
    val errorMessage: String? = null,
    val totalBooksOnList: Int? = null,
    val pages: Int,
    val hasCashed: Boolean = false,
    val isLoadingNext: Boolean = false,
    val totalBooksCount: Int? = null,
    val booksDbCount: Int? = null,
    val isLoading: Boolean? = null,
    val apiPages: Int,
    val isRefreshing: Boolean = false


) : ViewState

sealed class Event : ViewEvent {
    data object GetInitialBooks : Event()
    data class GetMoreBooks(val page: Int) : Event()
    data class LoadBooks(val page: Int) : Event()
    data class NavigateToInfo(val id: Int, val navController: NavController) : Event()
    data class SaveToDb(val books: List<BookDomain>) : Event()
    data object ClearCache : Event()
    data class UpdatePage(val pages: Int) : Event()
}

sealed class Effect : ViewSideEffect {
    data class SaveToDB(val books: List<BookDomain>) : Effect()
    data class LoadBooks(val page: Int) : Effect()
    data class GetMoreBooks(val page: Int) : Effect()
}

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val booksInteractor: BooksInteractor,
) : MviViewModel<Event, State, Effect>() {
    private val booksList = mutableListOf<BookDomain>()

    override fun setInitialState(): State = State(
        pages = 1,
        isLoading = true,
        apiPages = 1
    )

    override fun handleEvents(event: Event) {
        when (event) {
            is Event.GetInitialBooks -> {
                setState { copy(isLoading = true) }
                getRoomBooks(viewState.value.pages)
            }

            is Event.GetMoreBooks -> {
                getMoreBooksFromApi(viewState.value.apiPages)
                setState {
                    copy(apiPages = viewState.value.apiPages + 1)
                }
            }

            is Event.LoadBooks -> {
                setState { copy(isLoading = viewState.value.pages == 1) }
                getRoomBooks(event.page)
            }

            is Event.NavigateToInfo -> {
                event.navController.navigate("/book/${event.id}")
            }

            is Event.SaveToDb -> {
                viewModelScope.launch(Dispatchers.IO) {
                    booksInteractor.saveBooksToDb(event.books)
                    delay(2000)
                }
                setEffect { Effect.LoadBooks(viewState.value.pages) }
            }

            is Event.ClearCache -> {
                viewModelScope.launch(Dispatchers.IO) {
                    booksInteractor.clearCache()
                }
            }

            is Event.UpdatePage -> {
                getAllBooks()
                setState {
                    copy(pages = event.pages + 1)
                }
                setEffect { Effect.LoadBooks(viewState.value.pages) }
            }
        }
    }

    private fun getAllBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            booksInteractor.retrieveRoomBooks().collect {
                when (it) {
                    is BooksPartialState.Success -> {
                        setState { copy(booksDbCount = it.totalCount) }
                    }

                    is BooksPartialState.Failed -> {
                        setState { copy(errorMessage = it.errorMessage) }
                    }
                }
            }
            delay(1000)
        }
    }

    private fun getMoreBooksFromApi(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            setState { copy(isLoadingNext = true) }
            booksInteractor.retrieveNextBooks(page).collect {
                when (it) {
                    is BooksPartialState.Success -> {
                        setEffect { Effect.SaveToDB(it.books ?: listOf()) }
                        setState { copy(isLoadingNext = false) }

                    }

                    is BooksPartialState.Failed -> {
                        setState {
                            copy(
                                errorMessage = it.errorMessage,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getRoomBooks(page: Int) {
        getAllBooks()
        setState {
            copy(
                isLoadingNext = viewState.value.pages > 1,
                isLoading = viewState.value.pages == 1
            )
        }
        viewModelScope.launch {
            booksInteractor.retrieveRoomBooksPaginated(8, page).collect {
                when (it) {
                    is BooksPartialState.Success -> {
                        it.books?.let { books -> booksList.addAll(books) }
                        setState {
                            copy(
                                listOfBooks = booksList.toSet().toList(),
                                isLoadingNext = false,
                                isLoading = false
                            )
                        }
                    }

                    is BooksPartialState.Failed -> {
                        setEffect { Effect.GetMoreBooks(viewState.value.pages) }
                        setState { copy(isLoading = false) }
                    }
                }
            }

            viewState.value.booksDbCount?.let { dbcount ->
                viewState.value.listOfBooks.let { bookList ->
                    if (dbcount - 1 == bookList.size && viewState.value.pages > 1) {
                        setEffect { Effect.GetMoreBooks(viewState.value.apiPages) }
                    }
                }
            }
        }
    }
}