package com.kubekbreha.fastreader

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    var mainTextView : TextView? = null

    var testBook: String = "Sure. In this tutorial, I’ll show how to declare, populate, and iterate through a Java String array, including the Java 5 for loop syntax. Because creating a String array is just like creating and using any other Java object array, these examples also work as more generic object array examples."
    val testBookArray = testBook.split(" ").toTypedArray()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainTextView = findViewById(R.id.main_text_view)

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            var i = 0
            override fun run() {
                replaceTex(testBookArray[i])
                i++

                handler.postDelayed(this, 500) //added this line
            }
        }, 500)
    }


    fun replaceTex(word: String){
        mainTextView!!.text = word
    }
}
