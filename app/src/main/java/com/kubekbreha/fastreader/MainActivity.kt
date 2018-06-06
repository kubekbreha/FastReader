package com.kubekbreha.fastreader

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import com.itextpdf.text.pdf.PdfReader
import android.content.Intent
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory






class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private var testBook: String = "Sure. In this tutorial, I’ll show how to declare, populate, and" +
            " iterate through a Java String array, including the Java 5 for loop syntax. Because " +
            "creating a String array is just like creating and using any other Java object array," +
            " these examples also work as more generic object array examples. Sure. In this tutorial," +
            " I’ll show how to declare, populate, and iterate through a" +
            "Sure. In this tutorial, I’ll show how to declare, populate, and iterate through a Java String array, " +
            " Java String array, Sure. In this tutorial, I’ll show how to declare, populate, and " +
            "iterate through a Java String array, Sure. In this tutorial, I’ll show how to " +
            "declare, populate, and iterate through a Java String array, Sure. In this tutorial, I’ll " +
            "show how to declare, populate, and iterate through a Java String array, Sure. In this " +
            "tutorial, I’ll show how to declare, populate, and iterate through a Java String array, "
    private val testBookArray = testBook.split(" ").toTypedArray()

    private var delayMilis: Long = 1500
    private var wordCounter = 0
    private var running = false

    private var handler: Handler? = null
    private var handlerTask: Runnable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activity_main_seekBar.setOnSeekBarChangeListener(this)
        activity_main_running_imageButton.setOnClickListener(this)
        activity_main_plusTen_button.setOnClickListener(this)
        activity_main_minusTen_button.setOnClickListener(this)
        activity_main_restart_imageButton.setOnClickListener(this)





        handler = Handler()
        handlerTask = object : Runnable {
            override fun run() {
                replaceTex(testBookArray[wordCounter])
                if (testBookArray.size - 1 > wordCounter) {
                    wordCounter++
                }
                handler!!.postDelayed(this, delayMilis)

                activity_main_currentWord_textView.text = wordCounter.toString()
                Log.d("main", delayMilis.toString())
            }
        }


    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.activity_main_running_imageButton -> {
                if (running) {
                    handler!!.removeCallbacks(handlerTask)
                    running = false
                    activity_main_running_imageButton.setImageResource(R.drawable.ic_play)
                } else {
                    handler!!.post(handlerTask)
                    running = true
                    activity_main_running_imageButton.setImageResource(R.drawable.ic_pause)
                }
                Toast.makeText(this, running.toString(), Toast.LENGTH_SHORT).show()
            }
            R.id.activity_main_plusTen_button -> {
                if (wordCounter <= testBookArray.size - 11)
                    wordCounter += 10
                else
                    wordCounter = testBookArray.size - 1
            }
            R.id.activity_main_minusTen_button -> {
                if (wordCounter >= 10)
                    wordCounter -= 10
                else
                    wordCounter = 0


            }
            R.id.activity_main_restart_imageButton -> {
                wordCounter = 0
            }

            else -> {
            }
        }


    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        activity_main_currentSpeed_textView.text = delayMilis.toString() + " (milis between words)"
        delayMilis = 1200L - progress

    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}


    fun replaceTex(word: String) {
        activity_main_textView.text = word
    }
}



