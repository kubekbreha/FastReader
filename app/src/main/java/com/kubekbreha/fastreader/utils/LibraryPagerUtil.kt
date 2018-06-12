package com.kubekbreha.fastreader.utils

import android.view.View
import android.widget.TextView

import com.kubekbreha.fastreader.R
import com.kubekbreha.fastreader.library.Book


object LibraryPagerUtil {

    fun setupItem(view: View, book: Book) {
        val txt = view.findViewById(R.id.item_textView) as TextView
        txt.text = book.name

        //val img = view.findViewById(R.id.img_item) as ImageView
        //img.setImageResource(libraryObject.res)
    }

}
