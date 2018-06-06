package com.kubekbreha.fastreader

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private var testBook: String = "Sure. In this tutorial, I’ll show how to declare, populate, and iterate through a Java String array, including the Java 5 for loop syntax. Because creating a String array is just like creating and using any other Java object array, these examples also work as more generic object array examples."
    private val testBookArray = testBook.split(" ").toTypedArray()

    private var wordCounter = 0
    private var running = false

    private var handler: Handler? = null
    private var handlerTask: Runnable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activity_main_seekBar.setOnSeekBarChangeListener(this)
        activity_main_pause_button.setOnClickListener(this)


        handler = Handler()
        handlerTask = object : Runnable {
            override fun run() {
                replaceTex(testBookArray[wordCounter])
                if (testBookArray.size - 1 > wordCounter) {
                    wordCounter++
                }
                handler!!.postDelayed(this, 500)
            }
        }



    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.activity_main_pause_button -> {
                if (running) {
                    handler!!.removeCallbacks(handlerTask)
                    running = false
                } else {
                    handler!!.post(handlerTask)
                    running = true
                }
                Toast.makeText(this, running.toString(), Toast.LENGTH_SHORT).show()
            }

            else -> {
            }
        }


    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        activity_main_currentSpeed_textView.text = progress.toString() + "W/M"
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}


    fun replaceTex(word: String) {
        activity_main_textView.text = word
    }
}
