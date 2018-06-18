package com.kubekbreha.fastreader.library

import java.lang.ref.PhantomReference

class Book {

    var id : Int = 0
    lateinit var name: String
    lateinit var reference: String
    lateinit var image: ByteArray


    constructor(name: String, reference: String, image: ByteArray) {
        this.name = name
        this.reference = reference
        this.image = image
    }

    constructor()
}