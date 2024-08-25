package com.kitaplık.libraryservice.dto

data class AddBookRequest(
    val id: String, // id of the library
    val isbn: String //isbn number of the book which is going to be added
)
