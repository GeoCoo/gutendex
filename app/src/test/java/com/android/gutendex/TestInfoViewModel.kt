package com.android.gutendex

import com.android.gutendex.helpers.CoroutineTestRule
import com.android.gutendex.helpers.RobolectricTest
import com.android.gutendex.helpers.mockBookInfo
import com.android.gutendex.helpers.runFlowTest
import com.android.gutendex.helpers.runTest
import com.android.gutendex.helpers.toFlow
import com.android.gutendex.screens.info.BookInfoPartialState
import com.android.gutendex.screens.info.Event
import com.android.gutendex.screens.info.InfoInteractor
import com.android.gutendex.screens.info.InfoViewModel
import com.android.gutendex.screens.info.State
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import org.mockito.kotlin.times

class TestInfoViewModel : RobolectricTest() {

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    @Spy
    private lateinit var infoInteractor: InfoInteractor

    private lateinit var bookInfoViewModel: InfoViewModel
    private var initialState = State(isLoading = true)

    @Before
    fun before() {
        MockitoAnnotations.openMocks(this)
        bookInfoViewModel = InfoViewModel(infoInteractor)
    }


    @After
    fun after() {
        coroutineRule.cancelScopeAndDispatcher()
    }

    @Test
    fun `When Event#GetBookInfo and BookInfoPartialState#Success  then Assert State`() =
        coroutineRule.runTest {
            getBookInfoIntercetpr(BookInfoPartialState.Success(mockBookInfo))
            val event = Event.GetBookInfo("")
            bookInfoViewModel.setEvent(event)

            Mockito.verify(infoInteractor, times(1)).retrieveBookInfo("")

            bookInfoViewModel.viewStateHistory.runFlowTest {
                val state = awaitItem()
                TestCase.assertEquals(state, initialState.copy(isLoading = true))

                val state2 = awaitItem()
                TestCase.assertEquals(
                    state2,
                    initialState.copy(isLoading = false, bookInfo = mockBookInfo)
                )
            }
        }


    @Test
    fun `When Event#GetBookInfo and BookInfoPartialState#Failed  then Assert State`() =
        coroutineRule.runTest {
            getBookInfoIntercetpr(BookInfoPartialState.Success(mockBookInfo))
            val event = Event.GetBookInfo("")
            bookInfoViewModel.setEvent(event)

            Mockito.verify(infoInteractor, times(1)).retrieveBookInfo("")

            bookInfoViewModel.viewStateHistory.runFlowTest {
                val state = awaitItem()
                TestCase.assertEquals(state, initialState.copy(isLoading = true))

                val state2 = awaitItem()
                TestCase.assertEquals(
                    state2,
                    initialState.copy(
                        isLoading = false,
                        errorMessage = ArgumentMatchers.anyString()
                    )
                )

            }
        }

    private fun getBookInfoIntercetpr(partialState: BookInfoPartialState) {
        Mockito.`when`(infoInteractor.retrieveBookInfo(ArgumentMatchers.anyString()))
            .thenReturn(partialState.toFlow())
    }

}

