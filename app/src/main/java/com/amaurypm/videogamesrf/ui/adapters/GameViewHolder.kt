package com.amaurypm.videogamesrf.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.amaurypm.videogamesrf.data.remote.model.GameDto
import com.amaurypm.videogamesrf.databinding.GameElementBinding
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

class GameViewHolder (
    private  val binding: GameElementBinding
): RecyclerView.ViewHolder(binding.root){

    fun bind(game: GameDto){

        binding.tvTitle.text = game.name

      /*  Picasso.get()
            .load(game.thumbnail)
            .into(binding.ivThumbnail)*/

        Glide.with(binding.root.context)
            .load(game.thumbnail)
            .into(binding.ivThumbnail)
    }

}

