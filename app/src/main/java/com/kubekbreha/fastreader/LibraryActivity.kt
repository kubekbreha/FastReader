package com.kubekbreha.fastreader

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_library.*
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import android.content.Intent
import com.nbsp.materialfilepicker.MaterialFilePicker
import java.util.regex.Pattern


class LibraryActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        activity_library_add_book.setOnClickListener{
            MaterialFilePicker()
                    .withActivity(this)
                    .withRequestCode(1)
                    .withFilter(Pattern.compile(".*\\.pdf$")) // Filtering files and directories by file name using regexp
                    .withFilter(Pattern.compile(".*\\.epub$")) // Filtering files and directories by file name using regexp
                    .withHiddenFiles(true) // Show hidden files and folders
                    .start()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val filePath = data?.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)
            // Do anything with file
        }
    }

}
