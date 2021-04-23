package com.example.consumerapp.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.consumerapp.data.UserItems
import com.example.consumerapp.databinding.ItemRowUserBinding
import com.example.consumerapp.main.DetailUser

class FollowingAdapter(private var listFollowing: ArrayList<UserItems>) :
    RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserItems)
    }

    inner class FollowingViewHolder(private val binding: ItemRowUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(userItems: UserItems) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(userItems.avatar)
                    .apply(RequestOptions().override(100, 100))
                    .into(image)

                tvRepository.text = userItems.repository
                tvUsername.text = userItems.username
                tvLocation.text = userItems.location

                itemView.setOnClickListener {
                    onItemClickCallback?. onItemClicked(userItems)
                    Toast.makeText(
                        itemView.context,
                        "@${userItems.username}",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(itemView.context, DetailUser::class.java)
                    intent.putExtra(DetailUser.EXTRA_USER, userItems)
                    itemView.context.startActivity(intent)

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        holder.bind(listFollowing[position])
    }

    override fun getItemCount(): Int = listFollowing.size
}