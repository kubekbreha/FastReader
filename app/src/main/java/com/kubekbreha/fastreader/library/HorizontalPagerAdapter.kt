package com.kubekbreha.fastreader.library

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.kubekbreha.fastreader.R
import com.kubekbreha.fastreader.library.util.LibraryPagerUtil.setupItem
import com.kubekbreha.fastreader.reader.ReaderActivity
import org.jetbrains.anko.sdk25.coroutines.onClick


class HorizontalPagerAdapter(mContext: Context) : PagerAdapter() {

    private val database = DataBaseHandler(mContext)

    private val LIBRARY = database.readData()

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)

    private val _context = mContext

    override fun getCount(): Int {
        return LIBRARY.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = mLayoutInflater.inflate(R.layout.book_item, container, false)

        view.onClick {
            val intent = Intent(view.context, ReaderActivity::class.java)
            intent.putExtra("reference", LIBRARY[position].reference)
            view.context.startActivity(intent)
            (view.context as Activity).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            //Toast.makeText(_context ,LIBRARY[position].reference, Toast.LENGTH_SHORT).show()
        }

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


