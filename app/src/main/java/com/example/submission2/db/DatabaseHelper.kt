package com.example.submission2.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.submission2.db.DatabaseContract.FavoColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "dbfavoriteapp"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_FAV = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContract.FavoColumns.USERNAME} TEXT PRIMARY KEY  NOT NULL," +
                " ${DatabaseContract.FavoColumns.NAME} TEXT NOT NULL," +
                " ${DatabaseContract.FavoColumns.AVATAR} TEXT NOT NULL," +
                " ${DatabaseContract.FavoColumns.COMPANY} TEXT NOT NULL," +
                " ${DatabaseContract.FavoColumns.LOCATION} TEXT NOT NULL," +
                " ${DatabaseContract.FavoColumns.REPOSITORY} TEXT NOT NULL," +
                " ${DatabaseContract.FavoColumns.ISFAV} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_FAV)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}