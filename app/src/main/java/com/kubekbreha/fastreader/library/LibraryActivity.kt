package com.kubekbreha.fastreader.library

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.PopupMenu
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager
import com.kubekbreha.fastreader.R
import com.kubekbreha.fastreader.settings.SettingsActivity
import com.kubekbreha.fastreader.theme.util.ThemeUtil
import com.kubekbreha.fastreader.utils.reader.EpubFileReader
import com.kubekbreha.fastreader.utils.reader.PdfFileReader
import com.nbsp.materialfilepicker.MaterialFilePicker
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import kotlinx.android.synthetic.main.activity_library.*
import java.util.regex.Pattern


class LibraryActivity : AppCompatActivity(), View.OnClickListener {

    private val database = DataBaseHandler(this)
    private val PERMISSIONS_REQUEST_CODE = 0
    private lateinit var horizontalInfiniteCycleViewPager: HorizontalInfiniteCycleViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        val prefs = getSharedPreferences("theme_settings_shared_preference", Context.MODE_PRIVATE)
        SettingsActivity.mTheme = prefs.getInt("theme", 0)
        SettingsActivity.selectedTheme = prefs.getInt("theme", 0)
        SettingsActivity.mIsNightMode = prefs.getBoolean("darkMode", false)
        if (SettingsActivity.mIsNightMode) {
            delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        setTheme(ThemeUtil.getThemeId(SettingsActivity.mTheme))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        //hide status bar
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        //viewPager
        horizontalInfiniteCycleViewPager = findViewById(R.id.activity_library_view_pager)

        //buttons
        activity_library_add_book.setOnClickListener(this)
        activity_library_dots_button.setOnClickListener(this)

        //view pager
        setupViewPager()

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.activity_library_add_book -> {
                checkPermissionsAndOpenFilePicker()
            }

            R.id.activity_library_dots_button -> {
                val popupMenu = PopupMenu(this, activity_library_dots_button, Gravity.END)
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.settings -> {
                            val intent = Intent(this, SettingsActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                            true
                        }

                        R.id.develop -> {

                            true
                        }

                        else -> false
                    }
                }
                popupMenu.inflate(R.menu.menu_dots)
                try {
                    val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
                    fieldMPopup.isAccessible = true
                    val mPopup = fieldMPopup.get(popupMenu)
                    mPopup.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                            .invoke(mPopup, true)
                } catch (e: Exception) {
                    Log.e("Main", "Error showing icon menu, ", e)
                } finally {
                    popupMenu.show()
                }
                //popupMenu.show()
            }
            else -> {
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openFilePicker()
                } else {
                    showError()
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val filePath = data?.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)

            //get file name
            val fileNameCore = filePath!!.replace(".epub", "").replace(".pdf", "")
            val fileNameReversed = fileNameCore.reversed()

            val end = fileNameReversed.indexOf("/")
            var fileName = ""
            if (end != -1) {
                fileName = fileNameReversed.substring(0, end)
            }
            Toast.makeText(this, fileName.reversed(), Toast.LENGTH_SHORT).show()

            //insert to database
            if(filePath.toString()[filePath.toString().length - 1] == 'b') {
                database.insertData(Book(fileName.reversed(), filePath.toString(), EpubFileReader().getImage(filePath.toString(), this)!!))
            }else{
                database.insertData(Book(fileName.reversed(), filePath.toString(), PdfFileReader().pdfToBitmap(filePath.toString())))
            }
            //setup viewPager again because of missing just added book
            setupViewPager()
        }


    }


    private fun checkPermissionsAndOpenFilePicker() {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                showError()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(permission), PERMISSIONS_REQUEST_CODE)
            }
        } else {
            openFilePicker()
        }
    }


    private fun openFilePicker() {
        MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(1)
                .withFilter(Pattern.compile(".*\\.pdf$|.*\\.epub\$")) // Filtering files and directories by file name using regexp
                .withHiddenFiles(true) // Show hidden files and folders
                .start()
    }


    private fun showError() {
        Toast.makeText(this, "Allow external storage reading", Toast.LENGTH_SHORT).show()
    }


    fun setupViewPager() {
        horizontalInfiniteCycleViewPager.adapter = HorizontalPagerAdapter(this)
        horizontalInfiniteCycleViewPager.scrollDuration = 600
        horizontalInfiniteCycleViewPager.interpolator = AnimationUtils.loadInterpolator(this, android.R.anim.overshoot_interpolator)
        horizontalInfiniteCycleViewPager.isMediumScaled = false
        horizontalInfiniteCycleViewPager.maxPageScale = 0.8F
        horizontalInfiniteCycleViewPager.minPageScale = 0.5F
        horizontalInfiniteCycleViewPager.centerPageScaleOffset = 30.0F
        horizontalInfiniteCycleViewPager.minPageScaleOffset = 5.0F
        horizontalInfiniteCycleViewPager.currentItem = horizontalInfiniteCycleViewPager.realItem + 1
    }

}