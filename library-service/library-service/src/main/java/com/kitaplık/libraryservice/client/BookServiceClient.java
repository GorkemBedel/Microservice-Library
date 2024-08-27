package com.kitaplık.libraryservice.client;

import com.kitaplık.libraryservice.dto.BookDto;
import com.kitaplık.libraryservice.dto.BookIdDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.constraints.NotEmpty;

@FeignClient(name = "book-service", path = "/v1/book")
public interface BookServiceClient {

    Logger logger = LoggerFactory.getLogger(BookServiceClient.class);

    @GetMapping("/isbn/{isbn}")
    @CircuitBreaker(name = "getBookByIsbnCircuitBreaker", fallbackMethod = "getBookFallback")
    ResponseEntity<BookIdDto> getBookByIsbn(@PathVariable String isbn);

    default ResponseEntity<BookIdDto> getBookFallback(String isbn, Exception e) {
        logger.info("Book not found with ISBN: " + isbn + "returning default BookDto object");
        return ResponseEntity.ok(new BookIdDto("default-bookId", isbn));
    }

    @GetMapping("/book/{id}")
    @CircuitBreaker(name = "getBookByIdCircuitBreaker", fallbackMethod = "getBookByIdFallback")
    ResponseEntity<BookDto> getBookById(@PathVariable String id);

    default ResponseEntity<BookDto> getBookByIdFallback(String id, Exception exception) {
        logger.info("Book not found by id " + id + ", returning default BookDto object");
        return ResponseEntity.ok(new BookDto(new BookIdDto("default-book", "isbn")));
    }
}
