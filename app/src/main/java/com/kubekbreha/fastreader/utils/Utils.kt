package com.kubekbreha.fastreader.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.kubekbreha.fastreader.R


object Utils {

    fun setupItem(view: View, libraryObject: LibraryObject) {
        val txt = view.findViewById(R.id.txt_item) as TextView
        txt.text = libraryObject.title

        //val img = view.findViewById(R.id.img_item) as ImageView
        //img.setImageResource(libraryObject.res)
    }

    class LibraryObject(var res: Int, var title: String?)
}
