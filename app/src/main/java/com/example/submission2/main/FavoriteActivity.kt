package com.example.submission2.main

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission2.adapter.FavAdapter
import com.example.submission2.data.Favorite
import com.example.submission2.databinding.ActivityFavoriteBinding
import com.example.submission2.db.DatabaseContract.FavoColumns.Companion.CONTENT_URI
import com.example.submission2.db.FavHelper
import com.example.submission2.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class FavoriteActivity : AppCompatActivity() {
    private lateinit var adapter: FavAdapter

    private lateinit var binding: ActivityFavoriteBinding

    companion object{
        private const val EXTRA_STATE = "extra_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite Users"

        binding.rvUsersFav.layoutManager = LinearLayoutManager(this)
        binding.rvUsersFav.setHasFixedSize(true)
        adapter = FavAdapter(this)
        binding.rvUsersFav.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadFavAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
        if (savedInstanceState == null){
            loadFavAsync()
        }else {
            savedInstanceState.getParcelableArrayList<Favorite>(EXTRA_STATE)?.also { adapter.listFav = it }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFav)
    }


    private fun loadFavAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBarFav.visibility = View.VISIBLE
            val favHelper = FavHelper.getInstance(applicationContext)
            favHelper.open()
            val defferedFav = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor!!)
            }
            binding.progressBarFav.visibility = View.INVISIBLE
            val favs = defferedFav.await()
            favHelper.close()
            if (favs.size > 0){
                adapter.listFav = favs
            } else {
                adapter.listFav = ArrayList()
                showSnakcbarMessage("No Favorite User")
            }
        }
    }

    private fun showSnakcbarMessage(m: String) {
        Snackbar.make(binding.rvUsersFav, m, Snackbar.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        val aIntent = Intent(applicationContext, MainActivity::class.java)
        startActivity(aIntent)

    }
}