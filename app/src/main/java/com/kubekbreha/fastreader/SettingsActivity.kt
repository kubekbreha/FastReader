package com.kubekbreha.fastreader

import android.os.Bundle
import android.app.Activity
import android.util.Log
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_library.*
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.IOException


class SettingsActivity : Activity() {

    /** Called when the activity is first created.  */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //hide status bar
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    override fun onPause() {
        super.onPause()
        if (isFinishing) {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }


    }
}