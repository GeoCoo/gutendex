package com.android.gutendex.models

import com.android.gutendex.networking.Author
import com.android.gutendex.networking.BookResults
import com.android.gutendex.networking.BooksResponseDto
import com.android.gutendex.networking.SingeBookResponseDto

enum class NavRoutes(val route: String) {
    BOOKS("/books"), BOOK("/book/{id}"),

}

data class BookListing(
    val title: String? = "", val author: String? = "", val id: Int
)

data class BookInfo(
    val title: String,
    val author: String,
    val authorsBirth: String? = null,
    val subject: String
)


fun SingeBookResponseDto.toBookInfoDomain(): BookInfo {
    return BookInfo(
        title = this.title,
        author = this.authors.joinToString { it.name },
        authorsBirth = this.authors.joinToString { it.birth_year.toString() },
        subject = this.subjects.first()
    )
}

fun BooksResponseDto.toListOfBooks(): List<BookListing> {
    return this.results.map { bookResults ->
        bookResults.toBookListing()
    }
}

fun BookResults.toBookListing(): BookListing {
    return BookListing(
        title = this.title,
        author = this.authors.joinToString { it.name },
        id = this.id
    )
}

