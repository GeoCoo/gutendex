package com.android.gutendex.models

import com.android.gutendex.networking.BookResults
import com.android.gutendex.networking.BooksResponseDto
import com.android.gutendex.networking.SingeBookResponseDto
import com.android.gutendex.room.BookEntity

enum class NavRoutes(val route: String) {
    BOOKS("/books"), BOOK("/book/{id}")
}

data class BookDomain(
    val title: String? = "",
    val author: String? = "",
    val id: Int,
    val popularity: Int,
    val img: String? = ""
)

data class BookInfo(
    val title: String,
    val author: String,
    val authorsBirth: String? = null,
    val subject: String,
    val img: String?
)

fun SingeBookResponseDto.toBookInfoDomain(): BookInfo {
    return BookInfo(
        title = this.title,
        author = this.authors.joinToString { it.name },
        authorsBirth = this.authors.joinToString { it.birth_year.toString() },
        subject = this.subjects.first(),
        img = this.formats.image
    )
}

fun BooksResponseDto.toBooksDomain(): List<BookDomain> {
    return this.results.map { bookResults ->
        bookResults.toBookListing()
    }
}

fun List<BookEntity>.toBooksDomain(): List<BookDomain> {
    return this.map { bookEntity ->
        bookEntity.toBookListing()
    }
}

fun BookEntity.toBookListing(): BookDomain {
    return BookDomain(
        title = this.title,
        author = this.author,
        id = this.id,
        popularity = this.popularity,
        img = this.image
    )
}

fun BookResults.toBookListing(): BookDomain {
    return BookDomain(
        title = this.title,
        author = this.authors.joinToString { it.name },
        id = this.id,
        popularity = this.download_count,
        img = this.formats.image
    )
}

