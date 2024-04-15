package com.android.gutendex

import com.android.gutendex.helpers.CoroutineTestRule
import com.android.gutendex.helpers.RobolectricTest
import com.android.gutendex.helpers.mockBookInfo
import com.android.gutendex.helpers.runFlowTest
import com.android.gutendex.helpers.runTest
import com.android.gutendex.helpers.toFlow
import com.android.gutendex.screens.info.BookInfoPartialState
import com.android.gutendex.screens.info.BookInfoRepositoryResponse
import com.android.gutendex.screens.info.InfoInteractor
import com.android.gutendex.screens.info.InfoInteractorImpl
import com.android.gutendex.screens.info.InfoRepository
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.Spy

class TestInfoInteractor : RobolectricTest() {


    @get:Rule
    val coroutineRule = CoroutineTestRule()

    private lateinit var infoInteractor: InfoInteractor

    @Spy
    private lateinit var infoRepository: InfoRepository

    @Before
    fun before() {
        MockitoAnnotations.openMocks(this)

        infoInteractor = InfoInteractorImpl(infoRepository)

    }

    @After
    fun after() {
        coroutineRule.cancelScopeAndDispatcher()
    }

    @Test
    fun `Given Success from BookInfoRepositoryResponse, When retrieveBookInfo  is called, Then BookInfoPartialState Success emitted`() {
        coroutineRule.runTest {
            //Given
            getGameDetailsInterceptor(BookInfoRepositoryResponse.Success(mockBookInfo))
            //When
            infoInteractor.retrieveBookInfo("").runFlowTest {
                //Then
                TestCase.assertEquals(
                    BookInfoPartialState.Success(mockBookInfo),
                    awaitItem()
                )
            }
        }
    }

    @Test
    fun `Given Failed from BookInfoRepositoryResponse, When retrieveBookInfo  is called, Then BookInfoPartialState Failed emitted`() {
        coroutineRule.runTest {
            //Given
            getGameDetailsInterceptor(BookInfoRepositoryResponse.Failed("error"))
            //When
            infoInteractor.retrieveBookInfo("").runFlowTest {
                //Then
                TestCase.assertEquals(
                    BookInfoPartialState.Failed("error"),
                    awaitItem()
                )
            }
        }
    }

    @Test
    fun `Given Error from BookInfoRepositoryResponse, When retrieveBookInfo  is called, Then BookInfoPartialState Failed emitted`() {
        coroutineRule.runTest {
            //Given
            getGameDetailsInterceptor(BookInfoRepositoryResponse.Error("error"))
            //When
            infoInteractor.retrieveBookInfo("").runFlowTest {
                //Then
                TestCase.assertEquals(
                    BookInfoPartialState.Failed("error"),
                    awaitItem()
                )
            }
        }
    }


    private fun getGameDetailsInterceptor(repositoryResponse: BookInfoRepositoryResponse) {
        Mockito.`when`(infoRepository.getBookInfo(ArgumentMatchers.anyString()))
            .thenReturn(repositoryResponse.toFlow())
    }
}
