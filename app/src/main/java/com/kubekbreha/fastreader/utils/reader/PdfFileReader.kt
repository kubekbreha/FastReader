package com.kubekbreha.fastreader.utils.reader

import android.util.Log
import android.widget.Toast
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import java.io.File

class PdfFileReader : FileReader {

    fun getArrayOfWords(sampleFile: String, page: Int): Array<String> {
        var parsedText = ""

        try {
            val pdfReader = PdfReader(sampleFile)
            parsedText = parsedText + PdfTextExtractor.getTextFromPage(pdfReader, page + 1).trim() + "\n" //Extracting the content from the different pages
            Log.e("pdfReader", parsedText)
            pdfReader.close()
        } catch (e: Exception) {
            Log.e("pdfReader", "sakra prace sakra prace sakra prace sakra prace sakra prace$e")
        }

        return parsedText.split(" ").toTypedArray()
    }


    fun getPagesCount(sampleFile: String): Int {
        val pdfReader = PdfReader(sampleFile)
        return pdfReader.numberOfPages
    }

}