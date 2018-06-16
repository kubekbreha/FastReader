package com.kubekbreha.fastreader.settings

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.kubekbreha.fastreader.R
import android.content.SharedPreferences
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.widget.CompoundButton
import android.widget.LinearLayout
import com.kubekbreha.fastreader.theme.model.Theme
import com.kubekbreha.fastreader.theme.util.ThemeUtil
import com.kubekbreha.fastreader.theme.util.ThemeUtil.THEME_RED
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : AppCompatActivity()
        , View.OnClickListener {

    private lateinit var adapter: ThemeAdapter
    var mIsNightMode = false

    companion object {
        var selectedTheme = 0
        var mTheme = THEME_RED
        val viewColors = ArrayList<Theme>()

    }

    protected var mPrefs: SharedPreferences? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(ThemeUtil.getThemeId(mTheme))

        setContentView(R.layout.activity_settings)

        // data to populate the RecyclerView with
        viewColors.add(Theme(1, R.color.primaryColorPink, R.color.primaryDarkColorPink, R.color.secondaryColorPink))
        viewColors.add(Theme(2, R.color.primaryColorPurple, R.color.primaryDarkColorPurple, R.color.secondaryColorPurple))
        viewColors.add(Theme(3, R.color.primaryColorDeepPurple, R.color.primaryDarkColorDeepPurple, R.color.secondaryColorDeepPurple))
        viewColors.add(Theme(4, R.color.primaryColorIndigo, R.color.primaryDarkColorIndigo, R.color.secondaryColorIndigo))
        viewColors.add(Theme(5, R.color.primaryColorBlue, R.color.primaryDarkColorBlue, R.color.secondaryColorBlue))
        viewColors.add(Theme(6, R.color.primaryColorLightBlue, R.color.primaryDarkColorLightBlue, R.color.secondaryColorLightBlue))

        // set up the RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.activity_settings_recyclerView)
        val horizontalLayoutManagaer = LinearLayoutManager(this@SettingsActivity, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = horizontalLayoutManagaer
        adapter = ThemeAdapter(viewColors, object : RecyclerViewClickListener {
            override fun onClick(view: View, position: Int) {
                this@SettingsActivity.recreate()
            }
        })

        recyclerView.adapter = adapter


        //hide status bar
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        activity_settings_go_back.setOnClickListener(this)


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



    override fun onPause() {
        super.onPause()
        if (isFinishing) {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }


    }


}