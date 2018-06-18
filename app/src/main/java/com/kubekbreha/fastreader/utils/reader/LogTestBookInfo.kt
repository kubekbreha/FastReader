package com.kubekbreha.fastreader.utils.reader

import java.io.IOException
import nl.siegmann.epublib.domain.TOCReference
import nl.siegmann.epublib.epub.EpubReader
import android.app.Activity
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import com.kubekbreha.fastreader.R


/**
 * Log the info of 'assets/books/testbook.epub'.
 *
 * @author paul.siegmann
 */
class LogTestBookInfo : Activity() {
    /** Called when the activity is first created.  */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val assetManager = assets
        try {
            // find InputStream for book
            val epubInputStream = assetManager
                    .open("test.epub")

            // Load Book from inputStream
            val book = EpubReader().readEpub(epubInputStream)

            // Log the book's authors
            Log.e("epublib", "author(s): " + book.metadata.authors)

            // Log the book's title
            Log.e("epublib", "title: " + book.title)

            // Log the book's coverimage property
            val coverImage = BitmapFactory.decodeStream(book.coverImage
                    .inputStream)
            Log.e("epublib", "Coverimage is " + coverImage.width + " by "
                    + coverImage.height + " pixels")

            // Log the tale of contents
            logTableOfContents(book.tableOfContents.tocReferences, 0)
        } catch (e: IOException) {
            Log.e("epublib", e.message)
        }

    }

    /**
     * Recursively Log the Table of Contents
     *
     * @param tocReferences
     * @param depth
     */
    private fun logTableOfContents(tocReferences: List<TOCReference>?, depth: Int) {
        if (tocReferences == null) {
            return
        }
        for (tocReference in tocReferences) {
            val tocString = StringBuilder()
            for (i in 0 until depth) {
                tocString.append("\t")
            }
            tocString.append(tocReference.title)
            Log.i("epublib", tocString.toString())

            logTableOfContents(tocReference.children, depth + 1)
        }
    }
}
