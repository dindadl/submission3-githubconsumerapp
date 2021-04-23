package com.example.consumerapp.main

import android.annotation.SuppressLint
import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.consumerapp.R
import com.example.consumerapp.adapter.SectionsPagerAdapter
import com.example.consumerapp.data.Favorite
import com.example.consumerapp.data.UserItems
import com.example.consumerapp.databinding.ActivityDetailUserBinding
import com.example.consumerapp.db.DatabaseContract.FavoColumns.Companion.AVATAR
import com.example.consumerapp.db.DatabaseContract.FavoColumns.Companion.COMPANY
import com.example.consumerapp.db.DatabaseContract.FavoColumns.Companion.CONTENT_URI
import com.example.consumerapp.db.DatabaseContract.FavoColumns.Companion.ISFAV
import com.example.consumerapp.db.DatabaseContract.FavoColumns.Companion.LOCATION
import com.example.consumerapp.db.DatabaseContract.FavoColumns.Companion.NAME
import com.example.consumerapp.db.DatabaseContract.FavoColumns.Companion.REPOSITORY
import com.example.consumerapp.db.DatabaseContract.FavoColumns.Companion.USERNAME
import com.example.consumerapp.helper.MappingHelper
import com.google.android.material.tabs.TabLayoutMediator

class DetailUser : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailUserBinding
    private var favorite: Favorite? = null
    private var position: Int = 0
    private var isFav = false
    private lateinit var imgAvatar: String
    private lateinit var uriWithId: Uri

    companion object {
        var EXTRA_USER = "extra_user"
        var EXTRA_POSITION = "extra_position"
        var EXTRA_FAV = "extra_fav"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)


        favorite = intent.getParcelableExtra(EXTRA_FAV)
        Log.d("Favorite data: ", favorite.toString())
        if (favorite != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            setDataFav()
            isFav = true
            binding.btnFav.setImageResource(R.drawable.ic_baseline_favorite_24)
        }

        if (isFav){
            uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + favorite?.username)
            val cursor = contentResolver.query(uriWithId, null, null, null, null)
            if (cursor != null){
                favorite = MappingHelper.mapCursorToObject(cursor)
                cursor.close()
            }
        }
        val get = intent.getParcelableExtra<UserItems>(EXTRA_USER)
        val uname = get?.username
        val sectionsPagerAdapter = SectionsPagerAdapter(this, uname.toString())
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
        binding.btnFav.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    private fun setDataFav() {
        val favoriteUser = intent.getParcelableExtra<Favorite>(EXTRA_FAV)
        binding.detailName.text = checkNull(favoriteUser?.name)
        binding.detailUsername.text = "@" + checkNull(favoriteUser?.username)
        binding.detailRepository.text = checkNull(favoriteUser?.repository)
        binding.detailCompany.text = checkNull(favoriteUser?.company)
        binding.detailLocation.text = checkNull(favoriteUser?.location)
        Glide.with(this)
            .load(favoriteUser?.avatar)
            .into(binding.detailAvatar)

        imgAvatar = favoriteUser?.avatar.toString()
    }


    private fun checkNull(string: String?): String? {
        if (string == "null") {
            return "-"
        }
        return string
    }

    override fun onClick(view: View?) {
        val favTrue: Int = R.drawable.ic_baseline_favorite_24
        val favFalse: Int = R.drawable.ic_baseline_favorite_border_24
        if (view?.id == R.id.btn_fav) {
            if (isFav) {
                contentResolver.delete(uriWithId, null, null)
                Toast.makeText(this, "UnFavorite", Toast.LENGTH_SHORT).show()
                binding.btnFav.setImageResource(favFalse)
                isFav = false
                finish()

            } else {
                val favUsername = binding.detailUsername.text.toString().trim()
                val favName = binding.detailName.text.toString().trim()
                val favAvatar = imgAvatar
                val favCompany = binding.detailCompany.text.toString().trim()
                val favLocation = binding.detailLocation.text.toString().trim()
                val favRepo = binding.detailRepository.text.toString().trim()
                val favBool = "1"

                val values = ContentValues()
                values.put(USERNAME, favUsername)
                values.put(NAME, favName)
                values.put(AVATAR, favAvatar)
                values.put(COMPANY, favCompany)
                values.put(LOCATION, favLocation)
                values.put(REPOSITORY, favRepo)
                values.put(ISFAV, favBool)
                Log.d("Values ", values.toString())
                isFav = true
                binding.btnFav.setImageResource(favTrue)
                contentResolver.insert(CONTENT_URI, values)
                Toast.makeText(this, "Favorite", Toast.LENGTH_LONG).show()

            }
        }
    }
}