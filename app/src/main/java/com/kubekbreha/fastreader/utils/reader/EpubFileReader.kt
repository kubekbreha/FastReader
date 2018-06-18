package com.kubekbreha.fastreader.utils.reader

import android.util.Log
import com.github.mertakdut.Reader
import java.io.File

class EpubFileReader : com.kubekbreha.fastreader.utils.reader.FileReader {

    private var reader : Reader = Reader()

    /**
     * Get array with words from EPUB book.
     */
    fun getArrayOfWords(sampleFile :File, charsPerPage : Int, section: Int) : Array<String> {
        reader.setMaxContentPerSection(charsPerPage) // Max string length for the current page.
        reader.setIsIncludingTextContent(true) // Optional, to return the tags-excluded version.
        reader.setFullContent(sampleFile.getPath()) // Must call before readSection.

        val bookSection = reader.readSection(section)
        val sectionContent = bookSection.getSectionContent() // Returns content as html.
        val sectionTextContent = bookSection.getSectionTextContent() // Excludes html tags.

        Log.e("BOOK", sectionContent)
        Log.e("BOOK", sectionTextContent)
        Log.e("BOOK", sectionTextContent.length.toString())

        return sectionTextContent.split(" ").toTypedArray()
    }

}