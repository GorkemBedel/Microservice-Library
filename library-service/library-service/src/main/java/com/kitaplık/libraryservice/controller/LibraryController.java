package com.kitaplık.libraryservice.controller;

import com.kitaplık.libraryservice.dto.AddBookRequest;
import com.kitaplık.libraryservice.dto.LibraryDto;
import com.kitaplık.libraryservice.service.LibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RefreshScope
@RestController
@RequestMapping("/v1/library")
public class LibraryController {

    Logger logger = LoggerFactory.getLogger(LibraryController.class);
    private final LibraryService libraryService;
    private final Environment environment;
    @Value("${library-service.book.count}")
    private Integer count;


    public LibraryController(LibraryService libraryService, Environment environment) {
        this.libraryService = libraryService;
        this.environment = environment;
    }

    @GetMapping("{id}")
    public ResponseEntity<LibraryDto> getLibraryById(@PathVariable String id){
        return ResponseEntity.ok(libraryService.getAllBooksInLibraryById(id));
    }

    @GetMapping("/count")
    public ResponseEntity<String> getCount(){
        return ResponseEntity.ok("Library service count : " + count);
    }

    @PostMapping
    public ResponseEntity<LibraryDto> createLibrary(){
        logger.info("Library created on port number {}", environment.getProperty("local.server.port"));

        return ResponseEntity.ok(libraryService.createLibrary());
    }

    @PutMapping
    public ResponseEntity<Void> addBookToLibrary(@RequestBody AddBookRequest request){
        libraryService.addBookToLibrary(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<String>> getAllLibraries(){
        return ResponseEntity.ok(libraryService.getAllLibraries());
    }
}
