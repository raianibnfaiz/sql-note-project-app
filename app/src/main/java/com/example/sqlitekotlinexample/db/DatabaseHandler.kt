package com.example.sqlitekotlinexample.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.sqlitekotlinexample.models.Tasks
import java.util.*
@SuppressLint("Range")
class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable =
            "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY, $NAME TEXT,$DESC TEXT,$COMPLETED TEXT,$DEVELOPER TEXT,$TIME TEXT);"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val dropTable = "DROP TABLE IF EXISTS $TABLE_NAME"
        db.execSQL(dropTable)
        onCreate(db)
    }

    fun addTask(tasks: Tasks): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME, tasks.name)
        values.put(DESC, tasks.desc)
        values.put(COMPLETED, tasks.completed)
        values.put(DEVELOPER,tasks.developer)
        values.put(TIME,tasks.time)
        val success = db.insert(TABLE_NAME, null, values)
//        db.close()
        Log.v("InsertedId", "$success")
        return (Integer.parseInt("$success") != -1)
    }

    fun getTask(_id: Int): Tasks {
        val taskObj = Tasks()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)

        cursor?.moveToFirst()
        taskObj.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
        taskObj.name = cursor.getString(cursor.getColumnIndex(NAME))
        taskObj.desc = cursor.getString(cursor.getColumnIndex(DESC))
        taskObj.completed = cursor.getString(cursor.getColumnIndex(COMPLETED))
        taskObj.developer = cursor.getString((cursor.getColumnIndex(DEVELOPER)))
        taskObj.time = cursor.getString((cursor.getColumnIndex(TIME)))
        cursor.close()
        db.close()
        return taskObj
    }


    fun getAllTasks(): List<Tasks> {
        val taskList = ArrayList<Tasks>()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val tasks = Tasks()
                    tasks.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                    tasks.name = cursor.getString(cursor.getColumnIndex(NAME))
                    tasks.desc = cursor.getString(cursor.getColumnIndex(DESC))
                    tasks.completed = cursor.getString(cursor.getColumnIndex(COMPLETED))
                    tasks.developer = cursor.getString(cursor.getColumnIndex(DEVELOPER))
                    tasks.time = cursor.getString(cursor.getColumnIndex(TIME))
                    taskList.add(tasks)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return taskList
    }

    fun updateTask(tasks: Tasks): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME, tasks.name)
        values.put(DESC, tasks.desc)
        values.put(COMPLETED, tasks.completed)
        values.put(DEVELOPER,tasks.developer)
        values.put(TIME,tasks.time)
        val success = db.update(TABLE_NAME, values, "$ID=?", arrayOf(tasks.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$success") != -1
    }

    fun deleteTask(_id: Int): Boolean {
        val db = this.writableDatabase
        val success = db.delete(TABLE_NAME, "$ID=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$success") != -1
    }

    fun deleteAllTasks(): Boolean {
        val db = this.writableDatabase
        val success = db.delete(TABLE_NAME, null, null).toLong()
        db.close()
        return Integer.parseInt("$success") != -1
    }

    companion object {
        private const val DB_VERSION = 3
        private const val DB_NAME = "MyTasks"
        private const val TABLE_NAME = "Tasks"
        private const val ID = "Id"
        private const val NAME = "Name"
        private const val DESC = "Description"
        private const val COMPLETED = "Completed"
        private const val DEVELOPER = "Developer"
        private const val TIME = "Time"
    }
}