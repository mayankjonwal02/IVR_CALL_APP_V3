package com.example.ivr_call_app_v3.android.FunctionalComponents

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.ivr_call_app_v3.android.DataClasses.User


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "user_database"
        private const val DATABASE_VERSION = 1

        private const val TABLE_USER = "user"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_AGE = "age"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_USER_TABLE = ("CREATE TABLE $TABLE_USER ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_NAME TEXT,"
                + "$COLUMN_AGE INTEGER)")
        db.execSQL(CREATE_USER_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        onCreate(db)
    }

    // Insert a user into the database
    fun insertUser(name: String, age: Int) {
        val db = this.writableDatabase
        val query = "INSERT INTO $TABLE_USER ($COLUMN_NAME, $COLUMN_AGE) VALUES ('$name', $age)"
        db.execSQL(query)
        db.close()
    }

    // Get all users from the database
    fun getAllUsers(): List<User> {
        val userList = mutableListOf<User>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USER", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val age = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE))
                userList.add(User(id, name, age))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return userList
    }

    // Clear all users from the database
    fun clearUsers() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $TABLE_USER")
        db.close()
    }
}