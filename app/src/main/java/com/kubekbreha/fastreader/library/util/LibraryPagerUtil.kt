package com.kubekbreha.fastreader.library.util

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.kubekbreha.fastreader.R
import com.kubekbreha.fastreader.library.Book
import com.kubekbreha.fastreader.utils.reader.EpubFileReader


object LibraryPagerUtil {

    fun setupItem(view: View, book: Book, context: Context) {
        val txt = view.findViewById(R.id.book_item_textView) as TextView
        txt.text = book.name

        if(book.reference[book.reference.length - 1] == 'b'){
            val img = view.findViewById(R.id.book_item_roundedImageView) as ImageView
            img.setImageBitmap(EpubFileReader().getImage(book.reference, context))
        }
    }

}
