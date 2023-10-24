package network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import models.BookModel
import models.Links


val jsonClient = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}

suspend fun getBooks(): List<BookModel> {
    val request = jsonClient.get(Links.books)
    return request.body<List<BookModel>>()
}