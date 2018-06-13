package com.kubekbreha.fastreader

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.github.nisrulz.sensey.PinchScaleDetector
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.TouchTypeDetector
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

    //gestures
    private val LOGTAG = "TouchActivity"
    private val DEBUG = true
    private var txtResult: TextView? = null


    /**
     * On create. -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)

        //gestures
        Sensey.getInstance().init(this)
        txtResult = findViewById(R.id.activity_reader_textView)

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


        //start gestures detection
        startTouchTypeDetection()
        startPinchDetection()
    }

    override fun onPause() {
        super.onPause()
        if (isFinishing) {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        Sensey.getInstance().stopTouchTypeDetection()
        Sensey.getInstance().stopPinchScaleDetection()
    }

    override fun onDestroy() {
        super.onDestroy()

        Sensey.getInstance().stop()
    }


    override fun onResume() {
        super.onResume()

        startTouchTypeDetection()
        startPinchDetection()

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



    //gestrue functions
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        // Setup onTouchEvent for detecting type of touch gesture
        Sensey.getInstance().setupDispatchTouchEvent(event)
        return super.dispatchTouchEvent(event)
    }

    private fun resetResultInView(txt: TextView) {
        val handler = Handler()
        handler.postDelayed({ txt.text = "resultsShowHere" }, 3000)
    }

    private fun setResultTextView(text: String) {
        if (txtResult != null) {
            txtResult!!.setText(text)
            resetResultInView(txtResult!!)
            if (DEBUG) {
                Log.i(LOGTAG, text)
            }
        }
    }

    private fun startPinchDetection() {
        Sensey.getInstance()
                .startPinchScaleDetection(this@ReaderActivity, object : PinchScaleDetector.PinchScaleListener {
                    override fun onScale(scaleGestureDetector: ScaleGestureDetector, isScalingOut: Boolean) {
                        if (isScalingOut) {
                            setResultTextView("Scaling Out")
                        } else {
                            setResultTextView("Scaling In")
                        }
                    }

                    override fun onScaleEnd(scaleGestureDetector: ScaleGestureDetector) {
                        setResultTextView("Scaling : Stopped")
                    }

                    override fun onScaleStart(scaleGestureDetector: ScaleGestureDetector) {
                        setResultTextView("Scaling : Started")
                    }
                })
    }

    private fun startTouchTypeDetection() {
        Sensey.getInstance()
                .startTouchTypeDetection(this@ReaderActivity, object : TouchTypeDetector.TouchTypListener {
                    override fun onDoubleTap() {
                        setResultTextView("Double Tap")
                    }

                    override fun onLongPress() {
                        setResultTextView("Long press")
                    }

                    override fun onScroll(scrollDirection: Int) {
                        when (scrollDirection) {
                            TouchTypeDetector.SCROLL_DIR_UP -> setResultTextView("Scrolling Up")
                            TouchTypeDetector.SCROLL_DIR_DOWN -> setResultTextView("Scrolling Down")
                            TouchTypeDetector.SCROLL_DIR_LEFT -> setResultTextView("Scrolling Left")
                            TouchTypeDetector.SCROLL_DIR_RIGHT -> setResultTextView("Scrolling Right")
                            else -> {
                            }
                        }// Do nothing
                    }

                    override fun onSingleTap() {
                        setResultTextView("Single Tap")
                    }

                    override fun onSwipe(swipeDirection: Int) {
                        when (swipeDirection) {
                            TouchTypeDetector.SWIPE_DIR_UP -> setResultTextView("Swipe Up")
                            TouchTypeDetector.SWIPE_DIR_DOWN -> setResultTextView("Swipe Down")
                            TouchTypeDetector.SWIPE_DIR_LEFT -> setResultTextView("Swipe Left")
                            TouchTypeDetector.SWIPE_DIR_RIGHT -> setResultTextView("Swipe Right")
                            else -> {
                            }
                        }//do nothing
                    }

                    override fun onThreeFingerSingleTap() {
                        setResultTextView("Three Finger Tap")
                    }

                    override fun onTwoFingerSingleTap() {
                        setResultTextView("Two Finger Tap")
                    }
                })
    }



}


