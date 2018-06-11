package com.kubekbreha.fastreader.library

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_library.*
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import android.content.Intent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.kubekbreha.fastreader.R
import com.nbsp.materialfilepicker.MaterialFilePicker
import com.yarolegovich.discretescrollview.DSVOrientation
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import java.util.regex.Pattern




class LibraryActivity : AppCompatActivity() {


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
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val filePath = data?.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)
                Toast.makeText(this, filePath, Toast.LENGTH_SHORT).show()
        }
    }

}
