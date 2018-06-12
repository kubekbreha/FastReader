package com.kubekbreha.fastreader.library

import java.lang.ref.PhantomReference

class Book {

    lateinit var name: String
    lateinit var reference: String


    constructor(name: String, reference: String) {
        this.name = name
        this.reference = reference
    }

    constructor(){}
}