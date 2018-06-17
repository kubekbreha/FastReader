package com.kubekbreha.fastreader.utils.reader

import android.util.Log
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor

class PdfFileReader : FileReader {

    fun getArrayOfWords() {

        try {
            var parsedText = ""
            val pdfReader = PdfReader("assets/certificate.pdf")
            val n = pdfReader.getNumberOfPages()
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