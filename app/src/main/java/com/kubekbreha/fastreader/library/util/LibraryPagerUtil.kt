package com.kubekbreha.fastreader.library.util

import android.content.Context
import android.view.View
import android.widget.TextView

import com.kubekbreha.fastreader.library.Book
import android.graphics.BitmapFactory
import android.widget.ImageButton
import android.widget.ImageView
import com.kubekbreha.fastreader.R
import com.kubekbreha.fastreader.R.id.book_item_roundedImageView


object LibraryPagerUtil {

    fun setupItem(view: View, book: Book, context: Context) {
        val txt = view.findViewById(R.id.book_item_textView) as TextView
        txt.text = book.name

        val bitmap = BitmapFactory.decodeByteArray(book.image, 0, book.image.size)
        val image = view.findViewById(book_item_roundedImageView) as ImageView
        image.setImageBitmap(bitmap)

    }
}
