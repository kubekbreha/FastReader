package com.kubekbreha.fastreader.library

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.PopupMenu
import android.telecom.Call
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager
import com.kubekbreha.fastreader.R
import com.kubekbreha.fastreader.SettingsActivity
import com.nbsp.materialfilepicker.MaterialFilePicker
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import kotlinx.android.synthetic.main.activity_library.*
import kotlinx.android.synthetic.main.item.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.regex.Pattern


class LibraryActivity : AppCompatActivity(), View.OnClickListener {

    private val database = DataBaseHandler(this)
    val PERMISSIONS_REQUEST_CODE = 0
    lateinit var horizontalInfiniteCycleViewPager: HorizontalInfiniteCycleViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

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

    override fun onPause() {
        super.onPause()
        if (isFinishing) {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
            database.insertData(Book(fileName.reversed(), filePath.toString()))

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