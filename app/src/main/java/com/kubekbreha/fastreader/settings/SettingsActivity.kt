package com.kubekbreha.fastreader.settings

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import com.kubekbreha.fastreader.R


class SettingsActivity : Activity() {

    /** Called when the activity is first created.  */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

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