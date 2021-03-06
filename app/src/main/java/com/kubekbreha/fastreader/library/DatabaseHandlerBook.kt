package com.kubekbreha.fastreader.library

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast


val DATABASE_NAME ="FastReaderDatabase"
val TABLE_NAME="Books"
val COL_ID = "id"
val COL_NAME = "name"
val COL_REFERENCE = "reference"
val COL_IMAGE = "image"

class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context,DATABASE_NAME,null,1){
    override fun onCreate(db: SQLiteDatabase?) {

        val createTable = "CREATE TABLE " + TABLE_NAME +" (" +
                COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " VARCHAR(256)," +
                COL_REFERENCE +" VARCHAR(256)," +
                COL_IMAGE + " BLOB)"

        db?.execSQL(createTable)

    }

    override fun onUpgrade(db: SQLiteDatabase?,oldVersion: Int,newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun insertData(book : Book){
        val db = this.writableDatabase
        val cv = ContentValues()

        //name
        cv.put(COL_NAME,book.name)
        cv.put(COL_REFERENCE,book.reference)
        cv.put(COL_IMAGE,book.image)


        val result = db.insert(TABLE_NAME,null,cv)
        if(result == (-1).toLong())
            Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()
    }

    fun readData() : MutableList<Book>{
        val list : MutableList<Book> = ArrayList()

        val db = this.readableDatabase
        val query = "Select * from " + TABLE_NAME
        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do {
                var book = Book()
                book.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                book.name = result.getString(result.getColumnIndex(COL_NAME))
                book.reference = result.getString(result.getColumnIndex(COL_REFERENCE))
                book.image  = result.getBlob(result.getColumnIndex(COL_IMAGE))
                Log.e("dubug", book.image.toString())
                list.add(book)
            }while (result.moveToNext())
        }

        result.close()
        db.close()
        return list
    }

    fun deleteData() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close()
    }


}