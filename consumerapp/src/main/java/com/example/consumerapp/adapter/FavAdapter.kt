package com.example.consumerapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.consumerapp.CustomOnItemClickListener
import com.example.consumerapp.R
import com.example.consumerapp.data.Favorite
import com.example.consumerapp.databinding.ItemRowUserBinding
import com.example.consumerapp.main.DetailUser

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
