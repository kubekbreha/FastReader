package com.kubekbreha.fastreader

import android.content.res.TypedArray
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import com.github.mertakdut.Reader
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener, View.OnClickListener {


    private var testBookArray = arrayOf<String>()

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

        val sampleFile = getFileFromAssets("test1.epub")
        sampleFile.getPath()


        val reader = Reader()
        reader.setMaxContentPerSection(1000) // Max string length for the current page.
        reader.setIsIncludingTextContent(true) // Optional, to return the tags-excluded version.
        reader.setFullContent(sampleFile.getPath()) // Must call before readSection.

        val bookSection = reader.readSection(0)
        val sectionContent = bookSection.getSectionContent() // Returns content as html.
        val sectionTextContent = bookSection.getSectionTextContent() // Excludes html tags.


        Log.e("BOOK", sectionContent)
        testBookArray = sectionTextContent.split(" ").toTypedArray()

        

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


    fun getFileFromAssets(fileName: String): File {

        val file = File(cacheDir.toString() + "/" + fileName)

        if (!file.exists())
            try {

                val `is` = assets.open(fileName)
                val size = `is`.available()
                val buffer = ByteArray(size)
                `is`.read(buffer)
                `is`.close()

                val fos = FileOutputStream(file)
                fos.write(buffer)
                fos.close()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }

        return file
    }
}



