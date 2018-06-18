package com.kubekbreha.fastreader.utils.reader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.github.mertakdut.Reader
import com.itextpdf.text.pdf.qrcode.BitArray
import com.kubekbreha.fastreader.R
import nl.siegmann.epublib.domain.Author
import nl.siegmann.epublib.epub.EpubReader
import java.io.File
import android.support.v4.app.NotificationCompat.getExtras
import java.io.ByteArrayOutputStream
import android.support.v4.app.NotificationCompat.getExtras




class EpubFileReader : com.kubekbreha.fastreader.utils.reader.FileReader {


    fun getImage(sampleFile: String, context: Context): ByteArray? {
        val book = EpubReader()

        val epubInputStream = File(sampleFile).inputStream()
        try {
            val stream = ByteArrayOutputStream()
            BitmapFactory.decodeStream(book.readEpub(epubInputStream).coverImage
                    .inputStream).compress(Bitmap.CompressFormat.PNG, 100, stream)
            return stream.toByteArray()
        } catch (e: Exception) {
            Log.e("EpubFileReader", e.toString())

        }

        val stream = ByteArrayOutputStream()
        BitmapFactory.decodeResource(context.resources,
                R.drawable.test).compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    fun getAuthor(sampleFile: String): MutableList<Author>? {
        val book = EpubReader()

        val epubInputStream = File(sampleFile).inputStream()
        return book.readEpub(epubInputStream).metadata.authors
    }

    fun getTitle(sampleFile: String): String? {
        val book = EpubReader()

        val epubInputStream = File(sampleFile).inputStream()
        return book.readEpub(epubInputStream).title
    }


    /**
     * Get array with words from EPUB book.
     */
    fun getArrayOfWords(sampleFile: String, charsPerPage: Int, section: Int): Array<String> {
        val reader = Reader()

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