package com.kubekbreha.fastreader.theme

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.TaskStackBuilder
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SwitchCompat
import android.support.v7.widget.Toolbar
import android.transition.Explode
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import com.dynamictheme.MainController
import com.kubekbreha.fastreader.R

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var layout_green: LinearLayout? = null
    private var layout_purple: LinearLayout? = null
    private var switch_dark: SwitchCompat? = null
    private var isDarkTheme: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)

            //set the transition
            val ts = Explode()
            ts.duration = 5000
            window.enterTransition = ts
            window.exitTransition = ts
        }

        super.onCreate(savedInstanceState)

        setAppTheme()
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)


        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {
            TaskStackBuilder.create(this@MainActivity)
                    .addNextIntent(Intent(this@MainActivity,  MainActivity::class.java))
                    .addNextIntent(intent)
                    .startActivities()
        }


        switch_dark = findViewById(R.id.switch_darkTheme) as SwitchCompat
        layout_green = findViewById(R.id.Layout_green) as LinearLayout
        layout_purple = findViewById(R.id.Layout_purple) as LinearLayout


        switch_dark!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                isDarkTheme = true
                MainController.preferencePutBoolean(Dark_Theme, true)
            } else {
                isDarkTheme = false
                MainController.preferencePutBoolean(Dark_Theme, false)
            }
        }

        if (MainController.preferenceGetBoolean(Dark_Theme, false)) {
            switch_dark!!.isChecked = true
        }


        layout_green!!.setOnClickListener(this)
        layout_purple!!.setOnClickListener(this)


    }


    private fun setAppTheme() {

        if (MainController.preferenceGetString(Theme_Current, "") != "") {
            if (MainController.preferenceGetString(Theme_Current, "") == "Green") {
                setTheme(R.style.ThemeApp_Green)
            } else if (MainController.preferenceGetString(Theme_Current, "") == "Green_Dark") {
                setTheme(R.style.ThemeApp_Green_Dark)
            } else if (MainController.preferenceGetString(Theme_Current, "") == "Purple_Dark") {
                setTheme(R.style.ThemeApp_Purple_Dark)
            } else if (MainController.preferenceGetString(Theme_Current, "") == "Purple") {
                setTheme(R.style.ThemeApp_Purple)
            }
        } else {
            setTheme(R.style.ThemeApp_Green)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.Layout_green -> if (isDarkTheme)
                MainController.preferencePutString(Theme_Current, "Green_Dark")
            else
                MainController.preferencePutString(Theme_Current, "Green")

            R.id.Layout_purple ->

                if (isDarkTheme)
                    MainController.preferencePutString(Theme_Current, "Purple_Dark")
                else
                    MainController.preferencePutString(Theme_Current, "Purple")
        }/*TaskStackBuilder.create(this)
                        .addNextIntent(new Intent(this, Listactivity.class))
                        .addNextIntent(getIntent())
                        .startActivities();*/
    }

    companion object {
        private val Theme_Current = "ThemeCurrent"
        private val Dark_Theme = "DarkTheme"
    }
}
