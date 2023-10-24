package models

import kotlinx.serialization.Serializable

@Serializable
data class BookModel(val id: Long,
                val title: String,
                val annotation: String)