package com.kubekbreha.fastreader.utils.reader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import android.net.Uri
import android.util.Log
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import java.nio.ByteBuffer
import com.nbsp.materialfilepicker.utils.FileUtils
import android.opengl.ETC1.getHeight
import com.itextpdf.text.pdf.PdfPage
import android.os.Environment.getExternalStorageDirectory
import android.view.ViewTreeObserver
import android.webkit.WebView
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.support.v4.app.NotificationCompat.getExtras
import java.io.*


class PdfFileReader : FileReader {

    fun pdfToBitmap(pdfFileName: String): ByteArray {
        val pdfFile = File(pdfFileName)
        lateinit var bitmap: Bitmap
        try {
            val renderer = PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY))
            val page = renderer.openPage(0)

            val width = page.width
            val height = page.height
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            page.close()

            renderer.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }


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