package com.example.consumerapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Favorite(
    var username: String? = null,
    var name: String? = null,
    var avatar: String? = null,
    var company: String? = null,
    var location: String? = null,
    var repository: String? = null,
    var isFav: String? = null
): Parcelable
