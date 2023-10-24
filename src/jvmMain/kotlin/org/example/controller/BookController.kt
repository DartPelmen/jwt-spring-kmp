package org.example.controller

import models.BookModel
import models.Links
import org.example.service.BookService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class BookController(private val bookService: BookService) {
    @GetMapping(Links.books)
    fun getAllBooks(): List<BookModel> = bookService.getAll()
}