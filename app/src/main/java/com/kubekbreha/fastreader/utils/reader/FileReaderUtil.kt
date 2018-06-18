package com.kubekbreha.fastreader.utils.reader

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import nl.siegmann.epublib.domain.Author
import nl.siegmann.epublib.domain.Book
import nl.siegmann.epublib.epub.EpubReader
import java.io.IOException


class FileReaderUtil{

    lateinit var book: Book

    constructor(assets: AssetManager) {
        try {
            val assetManager = assets
            val epubInputStream = assetManager.open("test.epub")

            // Load Book from inputStream
            book = EpubReader().readEpub(epubInputStream)

        } catch (e: IOException) {
            Log.e("epublib", e.message)
        }
    }


    fun getImage(): Bitmap? {
        // Log the book's coverimage property
        return BitmapFactory.decodeStream(book.coverImage
                .inputStream)
    }

    fun getAuthor(): MutableList<Author>? {
        // Log the book's coverimage property
        return  book.metadata.authors
    }

    fun getTitle(): String? {
        // Log the book's coverimage property
        return  book.title
    }

}
