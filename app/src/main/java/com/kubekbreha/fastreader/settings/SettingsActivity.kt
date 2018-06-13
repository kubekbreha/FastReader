package com.kubekbreha.fastreader.settings

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.kubekbreha.fastreader.R
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : Activity(), ColorRecyclerAdapter.ItemClickListener
        , View.OnClickListener{

    private lateinit var adapter: ColorRecyclerAdapter

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //hide status bar
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        activity_settings_go_back.setOnClickListener(this)

        // data to populate the RecyclerView with
        val viewColors = ArrayList<Int>()
        viewColors.add(R.drawable.color_circle_yellow)
        viewColors.add(R.drawable.color_circle_purple)
        viewColors.add(R.drawable.color_circle_red)
        viewColors.add(R.drawable.color_circle_blue)
        viewColors.add(R.drawable.color_circle_green)
        viewColors.add(R.drawable.color_circle_pink)

        // set up the RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.activity_settings_recyclerView)
        val horizontalLayoutManagaer = LinearLayoutManager(this@SettingsActivity, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = horizontalLayoutManagaer
        adapter = ColorRecyclerAdapter(this, viewColors)
        adapter.setClickListener(this)
        recyclerView.adapter = adapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.activity_settings_go_back -> {
                finish()
            }

            else -> {
            }
        }
    }


    override fun onItemClick(view: View, position: Int) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on item position " + position, Toast.LENGTH_SHORT).show();

    }


    override fun onPause() {
        super.onPause()
        if (isFinishing) {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }


    }
}