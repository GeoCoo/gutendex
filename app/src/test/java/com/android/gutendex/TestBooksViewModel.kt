package com.android.gutendex

import com.android.gutendex.helpers.CoroutineTestRule
import com.android.gutendex.helpers.RobolectricTest
import com.android.gutendex.helpers.mockListBookDomain
import com.android.gutendex.helpers.runFlowTest
import com.android.gutendex.helpers.runTest
import com.android.gutendex.helpers.toFlow
import com.android.gutendex.screens.books.BooksInteractor
import com.android.gutendex.screens.books.BooksPartialState
import com.android.gutendex.screens.books.BooksViewModel
import com.android.gutendex.screens.books.Effect
import com.android.gutendex.screens.books.Event
import com.android.gutendex.screens.books.State
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import org.mockito.kotlin.times

class TestBooksViewModel : RobolectricTest() {

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    @Spy
    private lateinit var booksInteractor: BooksInteractor

    private lateinit var booksViewModel: BooksViewModel
    private var initialState = State(
        pages = 1, isLoading = true, apiPages = 1
    )

    @Before
    fun before() {
        MockitoAnnotations.openMocks(this)
        booksViewModel = BooksViewModel(booksInteractor)
    }


    @After
    fun after() {
        coroutineRule.cancelScopeAndDispatcher()
    }

    @Test
    fun `When Event#GetInitialBooks and BooksPartialState#Success  then Assert State`() =
        coroutineRule.runTest {
            getBooksIntercetor(
                BooksPartialState.Success(
                    mockListBookDomain, anyInt()
                )
            )
            val event = Event.GetInitialBooks
            booksViewModel.setEvent(event)

            Mockito.verify(booksInteractor, times(1)).retrieveRoomBooks()
            Mockito.verify(booksInteractor, times(1)).retrieveRoomBooksPaginated(anyInt(), anyInt())


            booksViewModel.viewStateHistory.runFlowTest {
                val state = awaitItem()
                TestCase.assertEquals(state, initialState.copy(isLoading = true))

                val state2 = awaitItem()
                TestCase.assertEquals(
                    state2, initialState.copy(
                        listOfBooks = mockListBookDomain, isLoadingNext = false, isLoading = false
                    )
                )
            }
        }

    @Test
    fun `When Event#GetInitialBooks and BooksPartialState#Failed  then Assert State and Effects`() =
        coroutineRule.runTest {
            getBooksIntercetor(
                BooksPartialState.Failed(
                    "errorMessage"
                )
            )
            val event = Event.GetInitialBooks
            booksViewModel.setEvent(event)

            Mockito.verify(booksInteractor, times(1)).retrieveRoomBooks()
            Mockito.verify(booksInteractor, times(1)).retrieveRoomBooksPaginated(anyInt(), anyInt())


            booksViewModel.viewStateHistory.runFlowTest {
                val state = awaitItem()
                TestCase.assertEquals(state, initialState.copy(isLoading = true))

                booksViewModel.effect.runFlowTest {
                    TestCase.assertEquals(
                        awaitItem(), Effect.GetMoreBooks(anyInt())
                    )
                }
            }
        }

    @Test
    fun `When Event#GetMoreBooks and BooksPartialState#Success then Assert State and Effects`() =
        coroutineRule.runTest {
            getBooksNextIntercetor(
                BooksPartialState.Success(
                    mockListBookDomain, anyInt()
                )
            )
            val event = Event.GetMoreBooks(anyInt())
            booksViewModel.setEvent(event)

            Mockito.verify(booksInteractor, times(1)).retrieveNextBooks(anyInt())
            booksViewModel.viewStateHistory.runFlowTest {
                val state = awaitItem()
                TestCase.assertEquals(state, initialState.copy(isLoadingNext = true))

                booksViewModel.effect.runFlowTest {
                    TestCase.assertEquals(
                        awaitItem(), Effect.SaveToDB(mockListBookDomain)
                    )
                }
                val state2 = awaitItem()
                TestCase.assertEquals(state2, state.copy(isLoadingNext = false))
            }
        }

    @Test
    fun `When Event#GetMoreBooks and BooksPartialState#Failed then Assert State and Effects`() =
        coroutineRule.runTest {
            getBooksNextIntercetor(
                BooksPartialState.Failed(
                    "error"
                )
            )
            val event = Event.GetMoreBooks(anyInt())
            booksViewModel.setEvent(event)

            Mockito.verify(booksInteractor, times(1)).retrieveNextBooks(anyInt())
            booksViewModel.viewStateHistory.runFlowTest {
                val state = awaitItem()
                TestCase.assertEquals(state, initialState.copy(isLoadingNext = true))

                val state2 = awaitItem()
                TestCase.assertEquals(
                    state2, state.copy(isLoadingNext = false, errorMessage = "error")
                )
            }
        }

    @Test
    fun `When Event#SaveToDb  Assert State and Effects`() = coroutineRule.runTest {

        val event = Event.SaveToDb(mockListBookDomain)
        booksViewModel.setEvent(event)
        Mockito.verify(booksInteractor, times(1)).saveBooksToDb(mockListBookDomain)
    }


    private fun getBooksIntercetor(partialState: BooksPartialState) {
        Mockito.`when`(
            booksInteractor.retrieveRoomBooksPaginated(
                anyInt(), anyInt()
            )
        ).thenReturn(partialState.toFlow())
    }

    private fun getBooksNextIntercetor(partialState: BooksPartialState) {
        Mockito.`when`(
            booksInteractor.retrieveNextBooks(
                anyInt(),
            )
        ).thenReturn(partialState.toFlow())
    }


}