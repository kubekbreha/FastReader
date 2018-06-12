package com.kubekbreha.fastreader.library

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager
import com.kubekbreha.fastreader.R
import com.nbsp.materialfilepicker.MaterialFilePicker
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import kotlinx.android.synthetic.main.activity_library.*
import java.util.regex.Pattern


class LibraryActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        //add library button
        activity_library_add_book.setOnClickListener(this)

        //view pager
        setupViewPager()
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.activity_library_add_book -> {
                MaterialFilePicker()
                        .withActivity(this)
                        .withRequestCode(1)
                        .withFilter(Pattern.compile(".*\\.pdf$|.*\\.epub\$")) // Filtering files and directories by file name using regexp
                        .withHiddenFiles(true) // Show hidden files and folders
                        .start()
            }

            else -> {
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val filePath = data?.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)
            Toast.makeText(this, filePath, Toast.LENGTH_SHORT).show()
        }
    }


    fun setupViewPager(){
        val horizontalInfiniteCycleViewPager = findViewById<HorizontalInfiniteCycleViewPager>(R.id.activity_library_view_pager)
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