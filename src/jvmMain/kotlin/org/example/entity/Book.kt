package org.example.entity

import jakarta.persistence.*


@Entity
@Table(name = "books", schema = "public")
open class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open var id: Long = 0
    @Column(nullable = false)
    open var title: String = ""
    @Column(nullable = false)
    open var annotation: String = ""
}