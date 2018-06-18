package com.kubekbreha.fastreader.utils.reader

import android.util.Log
import android.widget.Toast
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import java.io.File

class PdfFileReader : FileReader {

    fun getArrayOfWords(sampleFile : String)  {

        try {
            var parsedText = ""
            val pdfReader = PdfReader(sampleFile)
            val n = pdfReader.getNumberOfPages()
            Log.e("pdfReader", n.toString())
            for (i in 0 until n) {
                parsedText = parsedText + PdfTextExtractor.getTextFromPage(pdfReader, i + 1).trim() + "\n" //Extracting the content from the different pages
            }
            Log.e("pdfReader", parsedText)
            pdfReader.close()
        } catch (e: Exception) {
            Log.e("pdfReader", "sakra prace sakra prace sakra prace sakra prace sakra prace$e")
        }

    }
}