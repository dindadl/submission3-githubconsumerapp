package com.example.submission2.helper

import android.database.Cursor
import com.example.submission2.data.Favorite
import com.example.submission2.db.DatabaseContract

object MappingHelper {
    fun mapCursorToArrayList(favCursor: Cursor): ArrayList<Favorite>{
        val favList = ArrayList<Favorite>()

        favCursor.apply {
            while (moveToNext()){
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoColumns.USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.FavoColumns.NAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoColumns.AVATAR))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.FavoColumns.COMPANY))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.FavoColumns.LOCATION))
                val repository = getString(getColumnIndexOrThrow(DatabaseContract.FavoColumns.REPOSITORY))
                val isfav = getString(getColumnIndexOrThrow(DatabaseContract.FavoColumns.ISFAV))

               favList.add(Favorite(username, name, avatar, company, location, repository,isfav))
            }
        }
        return favList
    }

    fun mapCursorToObject(favCursor: Cursor): Favorite {
        var favorite: Favorite
        favCursor.apply {
            moveToFirst()
            val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoColumns.USERNAME))
            val name = getString(getColumnIndexOrThrow(DatabaseContract.FavoColumns.NAME))
            val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoColumns.AVATAR))
            val company = getString(getColumnIndexOrThrow(DatabaseContract.FavoColumns.COMPANY))
            val location = getString(getColumnIndexOrThrow(DatabaseContract.FavoColumns.LOCATION))
            val repository = getString(getColumnIndexOrThrow(DatabaseContract.FavoColumns.REPOSITORY))
            val isfav = getString(getColumnIndexOrThrow(DatabaseContract.FavoColumns.ISFAV))
            favorite = Favorite(username, name, avatar, company, location, repository, isfav)

        }
        return favorite
    }
}