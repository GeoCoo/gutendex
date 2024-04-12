package com.android.gutendex.networking


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

const val baseUrl = "http://gutendex.com"

interface ApiService {

    @GET("/books")
    suspend fun retrieveBooksList(): Response<BooksResponseDto>

    @GET("/books/{id}")
    suspend fun retrieveBookById(
        @Path("id") id: String,
    ): Response<SingeBookResponseDto>
}


interface ApiClient {
    suspend fun retrieveBooksList(): Response<BooksResponseDto>
    suspend fun retrieveBookById(id: String): Response<SingeBookResponseDto>

}

class ApiClientImpl @Inject constructor(private val apiService: ApiService) : ApiClient {
    override suspend fun retrieveBooksList(): Response<BooksResponseDto> =
        apiService.retrieveBooksList()

    override suspend fun retrieveBookById(id: String): Response<SingeBookResponseDto> =
        apiService.retrieveBookById(id)


}