package com.example.todolistapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TaskDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "tasks.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE tasks(id INTEGER PRIMARY KEY AUTOINCREMENT, task TEXT, isCompleted INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS tasks")
        onCreate(db)
    }

    // Insert task into the database
    fun insertTask(task: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("task", task)
            put("isCompleted", 0) // Default to not completed
        }
        db.insert("tasks", null, values)
    }

    // Delete task from the database
    fun deleteTask(id: Int) {
        writableDatabase.delete("tasks", "id=?", arrayOf(id.toString()))
    }

    // Mark task as completed (1 for completed)
    fun markTaskCompleted(id: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("isCompleted", 1)
        }
        db.update("tasks", values, "id=?", arrayOf(id.toString()))
    }

    // Get all tasks
    fun getAllTasks(): ArrayList<Task> {
        val tasksList = ArrayList<Task>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tasks", null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val task = cursor.getString(1)
            val isCompleted = cursor.getInt(2)
            tasksList.add(Task(id, task, isCompleted == 1))
        }
        cursor.close()
        return tasksList
    }
}