package com.kubekbreha.fastreader

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import com.kubekbreha.fastreader.R.id.main_text_view

class MainActivity : AppCompatActivity() {

    var mainTextView : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainTextView = findViewById(R.id.main_text_view)

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            var i = 0
            override fun run() {
                replaceTex(i)
                i++

                handler.postDelayed(this, 500) //added this line
            }
        }, 500)
    }


    fun replaceTex(i: Int){
        mainTextView!!.text = i.toString()
    }
}
