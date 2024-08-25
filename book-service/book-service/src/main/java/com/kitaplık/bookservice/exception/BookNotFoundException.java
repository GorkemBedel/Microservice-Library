package com.kitaplık.bookservice.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String s) {
        super(s);
    }
}
