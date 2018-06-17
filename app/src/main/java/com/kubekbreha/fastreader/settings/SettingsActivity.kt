package com.kubekbreha.fastreader.settings

import android.content.Intent
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
import com.kubekbreha.fastreader.library.LibraryActivity
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

    public override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(ThemeUtil.getThemeId(mTheme))

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        // data to populate the RecyclerView with
        viewColors.clear()
        viewColors.addAll(ThemeUtil.themeList)

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

        adapter.notifyDataSetChanged()


        activity_settings_dark_mode_switch.setOnCheckedChangeListener(
                object : CompoundButton.OnCheckedChangeListener {
                    override fun onCheckedChanged(compoundButton: CompoundButton, b: Boolean) {
                        mIsNightMode = b

                        if (mIsNightMode) {
                            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        } else {
                            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        }

                    }
                })


    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.activity_settings_go_back -> {
                //start library activity
                val intent = Intent(this, LibraryActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                //finish the setting
                finish()
            }

            else -> {
            }
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        //start library activity
        val intent = Intent(this, LibraryActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        //finish the setting
        finish()
    }

    override fun onPause() {
        super.onPause()
        if (isFinishing) {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }


    }


}