package com.kubekbreha.fastreader.library

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.kubekbreha.fastreader.R
import com.kubekbreha.fastreader.utils.LibraryPagerUtil

import com.kubekbreha.fastreader.utils.LibraryPagerUtil.setupItem
import java.sql.DatabaseMetaData


class HorizontalPagerAdapter(mContext: Context) : PagerAdapter() {

    private val database = DataBaseHandler(mContext)

    private val LIBRARY = database.readData()

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)



    override fun getCount(): Int {
        return LIBRARY.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = mLayoutInflater.inflate(R.layout.item, container, false)
        setupItem(view, LIBRARY[position])
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}


