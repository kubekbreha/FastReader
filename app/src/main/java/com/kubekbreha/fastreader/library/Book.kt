package com.kubekbreha.fastreader.library


class Book {

    var id : Int = 0
    lateinit var name: String
    lateinit var reference: String


    constructor(name: String, reference: String) {
        this.name = name
        this.reference = reference
    }

    constructor()
}