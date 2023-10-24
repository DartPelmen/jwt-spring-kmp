package org.example.service

import models.BookModel
import org.example.repository.BookRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


@Service
class BookService(private val bookRepository: BookRepository){
    fun getAll(): List<BookModel> =
        bookRepository.findAll().map {
            BookModel(it.id,it.title,it.annotation)
        }
    fun getByPage(pageNum: Int, pageSize: Int): List<BookModel> =
        bookRepository.findAll(Pageable.ofSize(pageSize).withPage(pageNum)).map {
            BookModel(it.id,it.title,it.annotation)
        }.toList()
}