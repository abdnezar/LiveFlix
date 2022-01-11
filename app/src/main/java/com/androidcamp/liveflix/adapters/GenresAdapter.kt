package com.androidcamp.liveflix.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androidcamp.liveflix.databinding.GenreItemBinding
import com.androidcamp.liveflix.models.Channel

class GenresAdapter(val context: Context, val click: OnClick) : RecyclerView.Adapter<GenresAdapter.ViewHolder>() {
    private var list = emptyList<String>()

    // For View Binding
    class ViewHolder(val binding: GenreItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(GenreItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.genreName.text = list[position]

        holder.binding.root.setOnClickListener {
            click.onClickGenre(list[position])
        }
    }

    fun setData(data: List<String>) {
        list = data
        notifyDataSetChanged()
    }

    interface OnClick {
        fun onClickGenre(genre: String)
    }
}