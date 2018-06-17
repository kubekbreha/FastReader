package com.kubekbreha.fastreader.settings

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kubekbreha.fastreader.R

import com.kubekbreha.fastreader.theme.model.Theme
import com.kubekbreha.fastreader.theme.view.ThemeView

class ThemeAdapter(private val themeList: List<Theme>, private val mRecyclerViewClickListener: RecyclerViewClickListener) : RecyclerView.Adapter<ThemeAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View, private val mListener: RecyclerViewClickListener) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var themeView: ThemeView = view.findViewById(R.id.themeView) as ThemeView

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            mListener.onClick(view, adapterPosition)
            SettingsActivity.selectedTheme = adapterPosition
            SettingsActivity.mTheme = SettingsActivity.viewColors[adapterPosition].id
            themeView.isActivated = true
            this@ThemeAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.color_item, parent, false)

        return MyViewHolder(itemView, mRecyclerViewClickListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val theme = themeList[position]
        holder.themeView.setTheme(theme)

        holder.themeView.isActivated = SettingsActivity.selectedTheme == position
    }

    override fun getItemCount(): Int {
        return themeList.size
    }
}