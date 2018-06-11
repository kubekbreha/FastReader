package com.kubekbreha.fastreader.library

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gigamole.infinitecycleviewpager.VerticalInfiniteCycleViewPager
import com.kubekbreha.fastreader.R
import com.kubekbreha.fastreader.utils.Utils

import com.kubekbreha.fastreader.utils.Utils.setupItem


class HorizontalPagerAdapter(private val mContext: Context, private val mIsTwoWay: Boolean) : PagerAdapter() {

    private val LIBRARIES = arrayOf<Utils.LibraryObject>(Utils.LibraryObject(
            R.drawable.ic_strategy,
            "Strategy"
    ), Utils.LibraryObject(
            R.drawable.ic_strategy,
            "Design"
    ), Utils.LibraryObject(
            R.drawable.ic_strategy,
            "Development"
    ), Utils.LibraryObject(
            R.drawable.ic_strategy,
            "Quality Assurance"
    ))
    private val mLayoutInflater: LayoutInflater

    init {
        mLayoutInflater = LayoutInflater.from(mContext)
    }

    override fun getCount(): Int {
        return if (mIsTwoWay) 6 else LIBRARIES.size
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


