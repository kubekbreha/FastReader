package com.kubekbreha.fastreader.library

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.kubekbreha.fastreader.R
import com.kubekbreha.fastreader.utils.LibraryPagerUtil

import com.kubekbreha.fastreader.utils.LibraryPagerUtil.setupItem


class HorizontalPagerAdapter(mContext: Context) : PagerAdapter() {

    private val LIBRARIES = arrayOf(LibraryPagerUtil.LibraryObject(
            R.drawable.ic_strategy,
            "Strategy"
    ), LibraryPagerUtil.LibraryObject(
            R.drawable.ic_strategy,
            "Design"
    ), LibraryPagerUtil.LibraryObject(
            R.drawable.ic_strategy,
            "Development"
    ), LibraryPagerUtil.LibraryObject(
            R.drawable.ic_strategy,
            "Quality Assurance"
    ))

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)



    override fun getCount(): Int {
        return LIBRARIES.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = mLayoutInflater.inflate(R.layout.item, container, false)
        setupItem(view, LIBRARIES[position])
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


