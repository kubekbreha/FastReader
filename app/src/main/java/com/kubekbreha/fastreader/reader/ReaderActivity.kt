package com.kubekbreha.fastreader.reader

import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.kubekbreha.fastreader.R
import com.kubekbreha.fastreader.settings.SettingsActivity
import com.kubekbreha.fastreader.theme.util.ThemeUtil
import com.kubekbreha.fastreader.utils.sensey.PinchScaleDetector
import com.kubekbreha.fastreader.utils.sensey.Sensey
import com.kubekbreha.fastreader.utils.sensey.TouchTypeDetector
import kotlinx.android.synthetic.main.activity_reader.*
import java.io.*
import android.content.Context
import android.util.TypedValue
import android.view.WindowManager
import com.kubekbreha.fastreader.utils.reader.EpubFileReader
import com.kubekbreha.fastreader.utils.reader.PdfFileReader


class ReaderActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    //array of words
    private var testBookArray = arrayOf<String>()

    private var delayMilis: Long = 1500
    private var wordCounter = 0
    private var running = false

    //variables for FileReader
    private var handler: Handler? = null
    private var handlerTask: Runnable? = null
    private val epubBookReader = EpubFileReader()
    private val pdfBookReader = PdfFileReader()

    //range variables for book reader
    private val charsPerPage: Int = 1000
    private var currentSection: Int = 0
    private var charsCount: Int = 0

    //gestures
    private var txtResult: TextView? = null


    /**
     * On create. -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(ThemeUtil.getThemeId(SettingsActivity.mTheme))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)

        val fileReference = intent.getStringExtra("reference")
        Toast.makeText(this, fileReference, Toast.LENGTH_SHORT).show()

        //hide status bar
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        //gestures
        Sensey.getInstance().init(this)
        txtResult = findViewById(R.id.activity_reader_textView)

        activity_reader_seekBar.progressDrawable.setColorFilter(getThemePrimaryColor(this), PorterDuff.Mode.SRC_IN)
        activity_reader_seekBar.thumb.setColorFilter(getThemePrimaryColor(this), PorterDuff.Mode.SRC_IN)

        //buttons listeners
        activity_reader_seekBar.setOnSeekBarChangeListener(this)
        activity_reader_running_imageButton.setOnClickListener(this)
        activity_reader_restart_imageButton.setOnClickListener(this)
        activity_reader_go_back.setOnClickListener(this)


        //check if pdf ot ebup
        val charArrayReference = fileReference.toCharArray()
        charArrayReference[fileReference.length - 1]


        //get array form book
        if(charArrayReference[fileReference.length - 1] == 'f'){
            pdfBookReader.getArrayOfWords(fileReference)
        }else if(charArrayReference[fileReference.length - 1] == 'b'){
            testBookArray = epubBookReader.getArrayOfWords(getFileFromAssets(""),
                    charsPerPage, currentSection)
        }


        activity_main_currentWord_textView.text = testBookArray.size.toString()

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
//        startPinchDetection()


    }

    private fun getThemePrimaryColor(context: Context): Int {
        val value = TypedValue()
        context.theme.resolveAttribute(R.attr.colorPrimary, value, true)
        return value.data
    }

    override fun onPause() {
        super.onPause()
        if (isFinishing) {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        Sensey.getInstance().stopTouchTypeDetection()
//        Sensey.getInstance().stopPinchScaleDetection()
    }

    override fun onDestroy() {
        super.onDestroy()

        Sensey.getInstance().stop()
    }


    override fun onResume() {
        super.onResume()

        startTouchTypeDetection()
//        startPinchDetection()

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



    //TODO NEED TO FIX THAT
    //gestrue functions
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        // Setup onTouchEvent for detecting type of touch gesture
        Sensey.getInstance().setupDispatchTouchEvent(event)
        return super.dispatchTouchEvent(event)
    }



    private fun startPinchDetection() {
        Sensey.getInstance()
                .startPinchScaleDetection(this@ReaderActivity, object : PinchScaleDetector.PinchScaleListener {
                    override fun onScale(scaleGestureDetector: ScaleGestureDetector, isScalingOut: Boolean) {
                        if (isScalingOut) {
                            //setResultTextView("Scaling Out")
                        } else {
                            //setResultTextView("Scaling In")
                        }
                    }

                    override fun onScaleEnd(scaleGestureDetector: ScaleGestureDetector) {
                        //setResultTextView("Scaling : Stopped")
                    }

                    override fun onScaleStart(scaleGestureDetector: ScaleGestureDetector) {
                        //setResultTextView("Scaling : Started")
                    }
                })
    }

    private fun startTouchTypeDetection() {
        Sensey.getInstance()
                .startTouchTypeDetection(this@ReaderActivity, object : TouchTypeDetector.TouchTypListener {
                    override fun onDoubleTap() {
                        //setResultTextView("Double Tap")
                        if (running) {
                            handler!!.removeCallbacks(handlerTask)
                            running = false
                            activity_reader_running_imageButton.setImageResource(R.drawable.ic_play)
                        } else {
                            handler!!.post(handlerTask)
                            running = true
                            activity_reader_running_imageButton.setImageResource(R.drawable.ic_pause)
                        }
                    }

                    override fun onLongPress() {
                        //setResultTextView("Long press")
                    }

                    override fun onScroll(scrollDirection: Int) {
                        when (scrollDirection) {
                            //TouchTypeDetector.SCROLL_DIR_UP -> setResultTextView("Scrolling Up")
                            //TouchTypeDetector.SCROLL_DIR_DOWN -> setResultTextView("Scrolling Down")
                            TouchTypeDetector.SCROLL_DIR_LEFT -> {

                            }

                            TouchTypeDetector.SCROLL_DIR_RIGHT -> {

                            }
                            else -> {
                            }
                        }// Do nothing
                    }

                    override fun onSingleTap() {
                        //setResultTextView("Single Tap")
                    }

                    override fun onSwipe(swipeDirection: Int) {
                        when (swipeDirection) {
                            //TouchTypeDetector.SWIPE_DIR_UP -> setResultTextView("Swipe Up")
                            //TouchTypeDetector.SWIPE_DIR_DOWN -> setResultTextView("Swipe Down")
                            TouchTypeDetector.SWIPE_DIR_LEFT -> {
                                if (wordCounter <= testBookArray.size - 11)
                                    wordCounter += 10
                                else
                                    wordCounter = testBookArray.size - 1
                                activity_main_currentWord_textView.text = wordCounter.toString()
                            }

                            TouchTypeDetector.SWIPE_DIR_RIGHT -> {
                                if (wordCounter >= 10)
                                    wordCounter -= 10
                                else
                                    wordCounter = 0
                                activity_main_currentWord_textView.text = wordCounter.toString()
                            }

                            else -> {
                            }
                        }//do nothing
                    }

                    override fun onThreeFingerSingleTap() {
                        //setResultTextView("Three Finger Tap")
                    }

                    override fun onTwoFingerSingleTap() {
                        //setResultTextView("Two Finger Tap")
                    }
                })
    }



}


