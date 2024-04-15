package com.android.gutendex

import com.android.gutendex.helpers.CoroutineTestRule
import com.android.gutendex.helpers.RobolectricTest
import com.android.gutendex.helpers.mockListBookDomain
import com.android.gutendex.helpers.runFlowTest
import com.android.gutendex.helpers.runTest
import com.android.gutendex.helpers.toFlow
import com.android.gutendex.room.RoomBookRepository
import com.android.gutendex.screens.books.BookInteractorImpl
import com.android.gutendex.screens.books.BooksInteractor
import com.android.gutendex.screens.books.BooksPartialState
import com.android.gutendex.screens.books.BooksRepository
import com.android.gutendex.screens.books.BooksRepositoryResponse
import junit.framework.TestCase
import kotlinx.coroutines.CoroutineScope
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.Spy

class TestsBooksIneractor : RobolectricTest() {

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    private lateinit var bookInteractor: BooksInteractor

    @Spy
    private lateinit var booksRepository: BooksRepository

    @Spy
    private lateinit var roomBookRepository: RoomBookRepository

    @Spy
    private lateinit var coroutineScope: CoroutineScope

    @Before
    fun before() {
        MockitoAnnotations.openMocks(this)

        bookInteractor = BookInteractorImpl(booksRepository, roomBookRepository, coroutineScope)

    }

    @After
    fun after() {
        coroutineRule.cancelScopeAndDispatcher()


    }


    @Test
    fun `Given Success from BooksRepositoryResponse, When retrieveBooks  is called, Then BooksPartialState Success emitted`() {
        coroutineRule.runTest {
            //Given
            getBooksRepoInterceptor(BooksRepositoryResponse.Success(mockListBookDomain, 3))
            //When
            bookInteractor.retrieveBooks(ArgumentMatchers.anyInt()).runFlowTest {
                //Then
                TestCase.assertEquals(
                    BooksPartialState.Success(mockListBookDomain, 3),
                    awaitItem()
                )
            }
        }
    }

    @Test
    fun `Given Failed from BooksRepositoryResponse, When retrieveBooks  is called, Then BooksPartialState Failed emitted`() {
        coroutineRule.runTest {
            //Given
            getBooksRepoInterceptor(BooksRepositoryResponse.Failed("error"))
            //When
            bookInteractor.retrieveBooks(ArgumentMatchers.anyInt()).runFlowTest {
                //Then
                TestCase.assertEquals(
                    BooksPartialState.Failed("error"),
                    awaitItem()
                )
            }
        }
    }

    @Test
    fun `Given Error from BooksRepositoryResponse, When retrieveBooks  is called, Then BooksPartialState Failed emitted`() {
        coroutineRule.runTest {
            //Given
            getBooksRepoInterceptor(BooksRepositoryResponse.Error("error"))
            //When
            bookInteractor.retrieveBooks(ArgumentMatchers.anyInt()).runFlowTest {
                //Then
                TestCase.assertEquals(
                    BooksPartialState.Failed("error"),
                    awaitItem()
                )
            }
        }
    }


    @Test
    fun `Given Success from BooksRepositoryResponse, When retrieveNextBooks  is called, Then BooksPartialState Success emitted`() {
        coroutineRule.runTest {
            //Given
            getBooksRepoInterceptor(
                BooksRepositoryResponse.Success(mockListBookDomain, ArgumentMatchers.anyInt())
            )
            //When
            bookInteractor.retrieveBooks(ArgumentMatchers.anyInt()).runFlowTest {
                //Then
                TestCase.assertEquals(
                    BooksPartialState.Success(mockListBookDomain, ArgumentMatchers.anyInt()),
                    awaitItem()
                )
            }
        }
    }

    @Test
    fun `Given Failed from BooksRepositoryResponse, When retrieveNextBooks  is called, Then BooksPartialState Success emitted`() {
        coroutineRule.runTest {
            //Given
            getBooksRepoInterceptor(
                BooksRepositoryResponse.Failed("error")
            )
            //When
            bookInteractor.retrieveBooks(ArgumentMatchers.anyInt()).runFlowTest {
                //Then
                TestCase.assertEquals(
                    BooksPartialState.Failed("error"),
                    awaitItem()
                )
            }
        }
    }

    @Test
    fun `Given  Error BooksRepositoryResponse, When retrieveNextBooks  is called, Then BooksPartialState Failed emitted`() {
        coroutineRule.runTest {
            //Given
            getBooksRepoInterceptor(
                BooksRepositoryResponse.Error("error")
            )
            //When
            bookInteractor.retrieveBooks(ArgumentMatchers.anyInt()).runFlowTest {
                //Then
                TestCase.assertEquals(
                    BooksPartialState.Failed("error"),
                    awaitItem()
                )
            }
        }
    }

    @Test
    fun `Given Success BooksRepositoryResponse, When retrieveRoomBooks  is called, Then BooksPartialState Success emitted`() {
        coroutineRule.runTest {
            //Given
            getRoomBooksRepoInterceptor(
                BooksRepositoryResponse.Success(mockListBookDomain, ArgumentMatchers.anyInt())
            )
            //When
            bookInteractor.retrieveRoomBooks().runFlowTest {
                //Then
                TestCase.assertEquals(
                    BooksPartialState.Success(mockListBookDomain, ArgumentMatchers.anyInt()),
                    awaitItem()
                )
            }
        }
    }

    @Test
    fun `Given Failed BooksRepositoryResponse, When retrieveRoomBooks  is called, Then BooksPartialState Failed emitted`() {
        coroutineRule.runTest {
            //Given
            getRoomBooksRepoInterceptor(
                BooksRepositoryResponse.Failed("error")
            )
            //When
            bookInteractor.retrieveRoomBooks().runFlowTest {
                //Then
                TestCase.assertEquals(
                    BooksPartialState.Failed("error"),
                    awaitItem()
                )
            }
        }
    }

    @Test
    fun `Given Error BooksRepositoryResponse, When retrieveRoomBooks  is called, Then BooksPartialState Failed emitted`() {
        coroutineRule.runTest {
            //Given
            getRoomBooksRepoInterceptor(
                BooksRepositoryResponse.Error("error")
            )
            //When
            bookInteractor.retrieveRoomBooks().runFlowTest {
                //Then
                TestCase.assertEquals(
                    BooksPartialState.Failed("error"),
                    awaitItem()
                )
            }
        }
    }


    @Test
    fun `Given Success BooksRepositoryResponse, When retrieveRoomBooksPaginated  is called, Then BooksPartialState Success emitted`() {
        coroutineRule.runTest {
            //Given
            getRoomBooksPaginatedRepoInterceptor(
                BooksRepositoryResponse.Success(mockListBookDomain, ArgumentMatchers.anyInt())
            )
            //When
            bookInteractor.retrieveRoomBooks().runFlowTest {
                //Then
                TestCase.assertEquals(
                    BooksPartialState.Success(mockListBookDomain, ArgumentMatchers.anyInt()),
                    awaitItem()
                )
            }
        }
    }


    @Test
    fun `Given Failed BooksRepositoryResponse, When retrieveRoomBooksPaginated  is called, Then BooksPartialState Failed emitted`() {
        coroutineRule.runTest {
            //Given
            getRoomBooksPaginatedRepoInterceptor(
                BooksRepositoryResponse.Failed("error")
            )
            //When
            bookInteractor.retrieveRoomBooks().runFlowTest {
                //Then
                TestCase.assertEquals(
                    BooksPartialState.Failed("error"),
                    awaitItem()
                )
            }
        }
    }

    @Test
    fun `Given Error BooksRepositoryResponse, When retrieveRoomBooksPaginated  is called, Then BooksPartialState Failed emitted`() {
        coroutineRule.runTest {
            //Given
            getRoomBooksPaginatedRepoInterceptor(
                BooksRepositoryResponse.Error("error")
            )
            //When
            bookInteractor.retrieveRoomBooks().runFlowTest {
                //Then
                TestCase.assertEquals(
                    BooksPartialState.Failed("error"),
                    awaitItem()
                )
            }
        }
    }


    private fun getBooksRepoInterceptor(repositoryResponse: BooksRepositoryResponse) {
        Mockito.`when`(booksRepository.getBooks(ArgumentMatchers.anyInt()))
            .thenReturn(repositoryResponse.toFlow())
    }

    private fun getRoomBooksRepoInterceptor(repositoryResponse: BooksRepositoryResponse) {
        Mockito.`when`(booksRepository.getBooks(ArgumentMatchers.anyInt()))
            .thenReturn(repositoryResponse.toFlow())
    }

    private suspend fun getRoomBooksPaginatedRepoInterceptor(repositoryResponse: BooksRepositoryResponse) {
        Mockito.`when`(
            roomBookRepository.getBooksPaged(
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt()
            )
        )
            .thenReturn(repositoryResponse.toFlow())
    }


}

