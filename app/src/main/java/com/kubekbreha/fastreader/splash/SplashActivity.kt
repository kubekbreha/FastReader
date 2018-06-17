package com.kubekbreha.fastreader.splash

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import android.view.WindowManager
import com.kubekbreha.fastreader.R
import com.kubekbreha.fastreader.library.LibraryActivity
import com.kubekbreha.fastreader.settings.SettingsActivity
import com.kubekbreha.fastreader.theme.util.ThemeUtil

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(ThemeUtil.getThemeId(SettingsActivity.mTheme))

        //hide status bar
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        val prefs = getSharedPreferences("theme_settings_shared_preference", Context.MODE_PRIVATE)
        SettingsActivity.mTheme = prefs.getInt("theme", 0)
        SettingsActivity.selectedTheme = prefs.getInt("theme", 0)
        SettingsActivity.mIsNightMode = prefs.getBoolean("darkMode", false)
        if (SettingsActivity.mIsNightMode) {
            delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        val intent = Intent(this, LibraryActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        //finish the setting
        finish()
    }
}
