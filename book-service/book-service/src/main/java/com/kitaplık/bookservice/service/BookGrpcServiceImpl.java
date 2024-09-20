package com.kitaplık.bookservice.service;

import com.kitaplik.bookservice.BookId;
import com.kitaplik.bookservice.BookServiceGrpc;
import com.kitaplik.bookservice.Isbn;
import com.kitaplık.bookservice.dto.BookIdDto;
import com.kitaplık.bookservice.exception.BookNotFoundException;
import com.kitaplık.bookservice.repository.BookRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BookGrpcServiceImpl extends BookServiceGrpc.BookServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(BookGrpcServiceImpl.class);
    private final BookRepository bookRepository;

    public BookGrpcServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void getBookIdByIsbn(Isbn request, StreamObserver<BookId> responseObserver) {
        logger.info("Grpc call received : " + request.getIsbn());
        BookIdDto bookIdDto = bookRepository.findByIsbn(request.getIsbn())
                .map(BookIdDto::convert)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ISBN: " + request.getIsbn()));
        responseObserver.onNext(
                BookId.newBuilder()
                        .setBookId(bookIdDto.getBookId())
                        .setIsnb(bookIdDto.getIsbn())
                        .build());
        responseObserver.onCompleted();
    }


}
