package com.kitaplık.libraryservice.dto

data class LibraryDto @JvmOverloads constructor(
    val id: String,
    val userBookList: List<BookDto>? = ArrayList(),
    val port: String? = "0"
)
