package com.kubekbreha.fastreader.theme.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

import com.kubekbreha.fastreader.R
import com.kubekbreha.fastreader.theme.model.Theme

/**
 * Created by Pankaj on 27-10-2017.
 */

class ThemeView : View {
    private var mTheme = Theme(R.color.primaryColorAmber, R.color.primaryDarkColorAmber, R.color.secondaryColorAmber)

    private var mBoarderPaint: Paint? = null
    private var mPrimaryPaint: Paint? = null
    private var mPrimaryDarkPaint: Paint? = null
    private var mAccentPaint: Paint? = null
    private var mBackgroundPaint: Paint? = null

    private var stroke: Float = 0.toFloat()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    fun setTheme(theme: Theme) {
        this.mTheme = theme
        init()
        invalidate()
    }

    private fun init() {
        try {
            mBoarderPaint = Paint()
            mBoarderPaint!!.style = Paint.Style.STROKE
            if (this.isSelected) {
                mBoarderPaint!!.color = Color.BLUE
            } else {
                mBoarderPaint!!.color = Color.GRAY
            }

            mBackgroundPaint = Paint()
            mBackgroundPaint!!.style = Paint.Style.FILL
            var color = android.R.color.background_light
            val a = TypedValue()
            context.theme.resolveAttribute(android.R.attr.windowBackground, a, true)
            if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT)
                color = a.data    // windowBackground is a color

            mBackgroundPaint!!.color = color!!

            mPrimaryDarkPaint = Paint()
            mPrimaryDarkPaint!!.style = Paint.Style.FILL
            mPrimaryDarkPaint!!.color = ContextCompat.getColor(context, mTheme.primaryDarkColor)

            mPrimaryPaint = Paint()
            mPrimaryPaint!!.style = Paint.Style.FILL
            mPrimaryPaint!!.color = ContextCompat.getColor(context, mTheme.primaryColor)

            mAccentPaint = Paint()
            mAccentPaint!!.style = Paint.Style.FILL
            mAccentPaint!!.color = ContextCompat.getColor(context, mTheme.accentColor)
            mAccentPaint!!.isAntiAlias = true
            mAccentPaint!!.isDither = true
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val height = height.toFloat()
        val width = width.toFloat()
        stroke = height * 8 / 100f
        val statusbar = height * 16 / 100f
        val toolbar = height * 72 / 100f

        if (this.isActivated) {
            mBoarderPaint!!.color = ContextCompat.getColor(context, R.color.themeSelected)
        } else {
            mBoarderPaint!!.color = ContextCompat.getColor(context, R.color.themeDeselected)
        }
        mBoarderPaint!!.strokeWidth = stroke
        canvas.drawRect(0f, 0f, width, height, mBackgroundPaint!!)
        canvas.drawRect(0f, 0f, width, statusbar, mPrimaryDarkPaint!!)
        canvas.drawRect(0f, statusbar, width, toolbar, mPrimaryPaint!!)
        canvas.drawCircle(width - stroke - height * 20 / 100f, toolbar, height * 16 / 100, mAccentPaint!!)
        canvas.drawRect(0f, 0f, width, height, mBoarderPaint!!)
    }
}
