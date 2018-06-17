package com.kubekbreha.fastreader.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.kubekbreha.fastreader.R
import android.content.SharedPreferences
import android.graphics.PorterDuff
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.util.TypedValue
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
    private var mIsNightMode = false

    companion object {
        var selectedTheme = 8
        var mTheme = 8
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

        activity_settings_dark_mode_switch.thumbDrawable.setColorFilter(getThemePrimaryColor(this), PorterDuff.Mode.MULTIPLY);
        activity_settings_dark_mode_switch.trackDrawable.setColorFilter(getThemePrimaryColor(this), PorterDuff.Mode.MULTIPLY);

        activity_settings_dark_mode_switch.setOnCheckedChangeListener { _, selected ->
            mIsNightMode = selected

            if (mIsNightMode) {
                delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

    }


    private fun getThemePrimaryColor(context: Context): Int {
        val value = TypedValue()
        context.theme.resolveAttribute(R.attr.colorPrimary, value, true)
        return value.data
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
        val editor = getSharedPreferences("theme_settings_shared_preference", Context.MODE_PRIVATE).edit()
        editor.putInt("theme", mTheme)
        editor.putBoolean("darkMode", mIsNightMode)
        editor.apply()


//        val prefs = getSharedPreferences("theme_settings_shared_preference", Context.MODE_PRIVATE)
//        mTheme = prefs.getInt("theme", 0)
//        mIsNightMode = prefs.getBoolean("darkMode", false)

    }


}