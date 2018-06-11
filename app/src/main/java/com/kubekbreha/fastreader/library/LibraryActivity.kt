package com.kubekbreha.fastreader.library

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager
import com.kubekbreha.fastreader.R
import com.nbsp.materialfilepicker.MaterialFilePicker
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import kotlinx.android.synthetic.main.activity_library.*
import java.util.regex.Pattern


class LibraryActivity : AppCompatActivity() {

    lateinit var infiniteCycleViewPager: HorizontalInfiniteCycleViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)


        activity_library_add_book.setOnClickListener{
            MaterialFilePicker()
                    .withActivity(this)
                    .withRequestCode(1)
                    .withFilter(Pattern.compile(".*\\.pdf$|.*\\.epub\$")) // Filtering files and directories by file name using regexp
                    .withHiddenFiles(true) // Show hidden files and folders
                    .start()
        }

        val horizontalInfiniteCycleViewPager = findViewById(R.id.activity_library_view_pager) as HorizontalInfiniteCycleViewPager
        horizontalInfiniteCycleViewPager.adapter = HorizontalPagerAdapter(this, false)

        //        horizontalInfiniteCycleViewPager.setScrollDuration(400);
        //        horizontalInfiniteCycleViewPager.setPageDuration(1000);
        //        horizontalInfiniteCycleViewPager.setInterpolator(
        //                AnimationUtils.loadInterpolator(getContext(), android.R.anim.overshoot_interpolator)
        //        );
        //        horizontalInfiniteCycleViewPager.setMediumScaled(false);
        //        horizontalInfiniteCycleViewPager.setMaxPageScale(0.8F);
        //        horizontalInfiniteCycleViewPager.setMinPageScale(0.5F);
        //        horizontalInfiniteCycleViewPager.setCenterPageScaleOffset(30.0F);
        //        horizontalInfiniteCycleViewPager.setMinPageScaleOffset(5.0F);
        //        horizontalInfiniteCycleViewPager.setOnInfiniteCyclePageTransformListener();

        //        horizontalInfiniteCycleViewPager.setCurrentItem(
        //                horizontalInfiniteCycleViewPager.getRealItem() + 1
        //        );


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val filePath = data?.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)
                Toast.makeText(this, filePath, Toast.LENGTH_SHORT).show()
        }
    }

}
