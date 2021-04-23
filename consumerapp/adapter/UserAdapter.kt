package com.example.submission2.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.submission2.data.UserItems
import com.example.submission2.databinding.ItemRowUserBinding
import com.example.submission2.main.DetailUser
import kotlin.collections.ArrayList


class UserAdapter(private val listUser: ArrayList<UserItems>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {


    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserItems)
    }

    inner class UserViewHolder(private val binding: ItemRowUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(userItems: UserItems) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(userItems.avatar)
                    .apply(RequestOptions().override(100, 100))
                    .into(image)

                tvRepository.text = checkNull(userItems.repository)
                tvUsername.text = "@" + checkNull(userItems.username)
                tvLocation.text = checkNull(userItems.location)

                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(userItems)
                    val intent = Intent(itemView.context, DetailUser::class.java)
                    intent.putExtra(DetailUser.EXTRA_USER, userItems)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    private fun checkNull(string: String?): String? {
        if (string == "null"){
            return "-"
        }
        return string
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

}