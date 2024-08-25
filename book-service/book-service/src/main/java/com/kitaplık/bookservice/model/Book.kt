package com.kitaplık.bookservice.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Table
import jakarta.persistence.Id
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "books")
data class Book @JvmOverloads constructor(

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    val id: String? = "",
    val title: String,
    val bookYear: Int,
    val author: String,
    val pressName: String,
    val isbn: String
){
    constructor() : this(null, "", 0, "", "", "") // Parametresiz constructor

}