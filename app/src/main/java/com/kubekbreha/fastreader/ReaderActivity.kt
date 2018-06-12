package com.kubekbreha.fastreader

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_reader.*
import java.io.*


class ReaderActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    //array of words
    private var testBookArray = arrayOf<String>()

    private var delayMilis: Long = 1500
    private var wordCounter = 0
    private var running = false

    //variables for BookReader
    private var handler: Handler? = null
    private var handlerTask: Runnable? = null
    private val epubBookReader = com.kubekbreha.fastreader.bookReader.EpubBookReader()

    //range variables for book reader
    private val charsPerPage: Int = 1000
    private var currentSection: Int = 0
    private var charsCount: Int = 0


    /**
    * On create. -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)

        //buttons listeners
        activity_reader_seekBar.setOnSeekBarChangeListener(this)
        activity_reader_running_imageButton.setOnClickListener(this)
        activity_reader_plusTen_button.setOnClickListener(this)
        activity_reader_minusTen_button.setOnClickListener(this)
        activity_reader_restart_imageButton.setOnClickListener(this)
        activity_reader_go_back.setOnClickListener(this)


        //get array form book
        testBookArray = epubBookReader.getArrayOfWordsInBook(getFileFromAssets("test1.epub"),
                charsPerPage, currentSection)


        activity_main_currentWord_textView.text = testBookArray.size.toString()
        //activity_main_textView.text = testBookArray[16297]


        //timer which schedule delay between words
        handler = Handler()
        handlerTask = object : Runnable {
            override fun run() {
                replaceTex(testBookArray[wordCounter])
                if (testBookArray.size - 1 > wordCounter) {
                    wordCounter++
                }
                handler!!.postDelayed(this, delayMilis)
                activity_main_currentWord_textView.text = wordCounter.toString()
            }
        }

    }

    override fun onPause() {
        super.onPause()
        if (isFinishing) {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    /**
     * On click listeners. -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
     */
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.activity_reader_running_imageButton -> {
                if (running) {
                    handler!!.removeCallbacks(handlerTask)
                    running = false
                    activity_reader_running_imageButton.setImageResource(R.drawable.ic_play)
                } else {
                    handler!!.post(handlerTask)
                    running = true
                    activity_reader_running_imageButton.setImageResource(R.drawable.ic_pause)
                }
                Toast.makeText(this, running.toString(), Toast.LENGTH_SHORT).show()
            }

            R.id.activity_reader_plusTen_button -> {
                if (wordCounter <= testBookArray.size - 11)
                    wordCounter += 10
                else
                    wordCounter = testBookArray.size - 1
                activity_main_currentWord_textView.text = wordCounter.toString()
            }

            R.id.activity_reader_minusTen_button -> {
                if (wordCounter >= 10)
                    wordCounter -= 10
                else
                    wordCounter = 0
                activity_main_currentWord_textView.text = wordCounter.toString()
            }

            R.id.activity_reader_restart_imageButton -> {
                wordCounter = 0
                activity_main_currentWord_textView.text = wordCounter.toString()
            }
            R.id.activity_reader_go_back -> {
               finish()
            }

            else -> {
            }
        }
    }




    /**
     * SeekBar functions.-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
     */
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        activity_reader_currentSpeed_textView.text = delayMilis.toString() + " (milis between words)"
        delayMilis = 1200L - progress
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
    override fun onStopTrackingTouch(seekBar: SeekBar?) {}


    /**
     * My functions. -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
     */
    fun replaceTex(word: String) {
        charsCount += word.length
        activity_reader_textView.text = word
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



