package com.kubekbreha.fastreader.utils.reader

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.github.mertakdut.Reader
import nl.siegmann.epublib.domain.Author
import nl.siegmann.epublib.domain.Book
import nl.siegmann.epublib.epub.EpubReader
import java.io.File
import java.io.IOException

class EpubFileReader : com.kubekbreha.fastreader.utils.reader.FileReader {

    private var reader : Reader = Reader()

    var book = EpubReader()


    fun getImage(sampleFile :String ): Bitmap? {
        val epubInputStream = File(sampleFile).inputStream()
        return BitmapFactory.decodeStream(book.readEpub(epubInputStream).coverImage
                .inputStream)
    }

    fun getAuthor(sampleFile :String): MutableList<Author>? {
        val epubInputStream = File(sampleFile).inputStream()
        return  book.readEpub(epubInputStream).metadata.authors
    }

    fun getTitle(sampleFile :String): String? {
        val epubInputStream = File(sampleFile).inputStream()
        return  book.readEpub(epubInputStream).title
    }



    /**
     * Get array with words from EPUB book.
     */
    fun getArrayOfWords(sampleFile :String, charsPerPage : Int, section: Int) : Array<String> {
        reader.setMaxContentPerSection(charsPerPage) // Max string length for the current page.
        reader.setIsIncludingTextContent(true) // Optional, to return the tags-excluded version.
        reader.setFullContent(sampleFile) // Must call before readSection.

        val bookSection = reader.readSection(section)
        val sectionTextContent = bookSection.sectionTextContent // Excludes html tags.

        Log.e("BOOK", sectionTextContent)
        Log.e("BOOK", sectionTextContent.length.toString())

        return sectionTextContent.split(" ").toTypedArray()
    }

}