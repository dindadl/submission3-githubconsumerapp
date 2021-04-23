package com.example.submission2.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.submission2.CustomOnItemClickListener
import com.example.submission2.R
import com.example.submission2.data.Favorite
import com.example.submission2.databinding.ItemRowUserBinding
import com.example.submission2.main.DetailUser

class FavAdapter(private val activity: Activity) :
    RecyclerView.Adapter<FavAdapter.FavViewHolder>() {
    var listFav = ArrayList<Favorite>()
        set(listFav) {
            if (listFav.size > 0) {
                this.listFav.clear()
            }
            this.listFav.addAll(listFav)

            notifyDataSetChanged()
        }

    fun addItem(favorite: Favorite) {
        this.listFav.add(favorite)
        notifyItemInserted(this.listFav.size - 1)
    }

    fun removeItem(position: Int) {
        this.listFav.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listFav.size)
    }

    inner class FavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowUserBinding.bind(itemView)
        fun bind(favorite: Favorite) {
            binding.tvUsername.text = favorite.username
            binding.tvLocation.text = favorite.location
            binding.tvRepository.text = favorite.repository
            Glide.with(itemView.context)
                .load(favorite.avatar)
                .apply(RequestOptions().override(100, 100))
                .into(binding.image)

            itemView.setOnClickListener(
                CustomOnItemClickListener(
                    adapterPosition,
                    object : CustomOnItemClickListener.OnItemClickCallback {
                        override fun onItemClicked(v: View?, position: Int) {
                            val intent = Intent(activity, DetailUser::class.java)
                            intent.putExtra(DetailUser.EXTRA_POSITION, position)
                            intent.putExtra(DetailUser.EXTRA_FAV, favorite)
                            activity.startActivity(intent)
                        }
                    })
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return FavViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        holder.bind(listFav[position])
    }

    override fun getItemCount(): Int = this.listFav.size


}
