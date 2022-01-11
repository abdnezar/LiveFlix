package com.androidcamp.liveflix.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androidcamp.liveflix.R
import com.androidcamp.liveflix.databinding.ChannelItemBinding
import com.androidcamp.liveflix.models.Channel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.bumptech.glide.request.target.Target
import jp.wasabeef.glide.transformations.BlurTransformation

class ChannelsAdapter(val context: Context, val click: OnClick) : RecyclerView.Adapter<ChannelsAdapter.ViewHolder>() {
    private var list = ArrayList<Channel>()

    // For View Binding
    class ViewHolder(val binding: ChannelItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ChannelItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val channel = list[position]
        holder.binding.channelName.text = channel.name

        val rand = (0..10).random()
        val image: Int = context.resources.getIdentifier("bg$rand", "drawable", context.packageName)

        Glide.with(context)
            .load(image)
            .apply(bitmapTransform(BlurTransformation(22)))
            .into(holder.binding.channelBg)

        Glide.with(context)
            .load(channel.logo)
            .addListener(object : RequestListener<Drawable> {
                @SuppressLint("UseCompatLoadingForDrawables")
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    holder.binding.channelImage.background = context.resources.getDrawable(R.drawable.ic_imovie,null)
                    return false
                }
                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    holder.binding.channelImage.background = null
                    holder.binding.channelImage.setImageDrawable(resource)
                    return true
                }
            })
            .into(holder.binding.channelImage)

        holder.binding.root.setOnClickListener {
            click.onClickChannel(channel.name, channel.url)
        }
    }

    fun setData(data: ArrayList<Channel>) {
        list.clear()
        list = data
        notifyDataSetChanged()
    }

    interface OnClick {
        fun onClickChannel(channelName: String, url: String)
    }



}