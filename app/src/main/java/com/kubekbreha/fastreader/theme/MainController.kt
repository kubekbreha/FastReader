package com.dynamictheme

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager


class MainController : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        sharedPreferencesEditor = sharedPreferences!!.edit()
        context = applicationContext
        //MultiDex.install(this);
    }

    companion object {

        private var appInstance: MainController? = null
        private var sharedPreferences: SharedPreferences? = null
        private var sharedPreferencesEditor: SharedPreferences.Editor? = null
        var context: Context? = null

        fun getAppInstance(): MainController {
            return appInstance!!
        }

        val applicationPreferenceEditor: SharedPreferences.Editor
            get() {
                    return sharedPreferences!!.edit()

            }

        val applicationPreference: SharedPreferences
            get() {
                return PreferenceManager.getDefaultSharedPreferences(context)

            }
        /**
         * Application level preference work.
         */

        fun preferencePutInteger(key: String, value: Int) {
            sharedPreferencesEditor!!.putInt(key, value)
            sharedPreferencesEditor!!.commit()
        }

        fun preferenceGetInteger(key: String, defaultValue: Int): Int {
            return sharedPreferences!!.getInt(key, defaultValue)
        }

        fun preferencePutBoolean(key: String, value: Boolean) {
            sharedPreferencesEditor!!.putBoolean(key, value)
            sharedPreferencesEditor!!.commit()
        }

        fun preferenceGetBoolean(key: String, defaultValue: Boolean): Boolean {
            return sharedPreferences!!.getBoolean(key, defaultValue)
        }

        fun preferencePutString(key: String, value: String) {
            sharedPreferencesEditor!!.putString(key, value)
            sharedPreferencesEditor!!.commit()
        }

        fun preferenceGetString(key: String, defaultValue: String): String? {
            return sharedPreferences!!.getString(key, defaultValue)
        }

        fun preferencePutLong(key: String, value: Long) {
            sharedPreferencesEditor!!.putLong(key, value)
            sharedPreferencesEditor!!.commit()
        }

        fun preferenceGetLong(key: String, defaultValue: Long): Long {
            return sharedPreferences!!.getLong(key, defaultValue)
        }

        fun preferenceRemoveKey(key: String) {
            sharedPreferencesEditor!!.remove(key)
            sharedPreferencesEditor!!.commit()
        }

        fun clearPreference() {
            sharedPreferencesEditor!!.clear()
            sharedPreferencesEditor!!.commit()
        }
    }

}
