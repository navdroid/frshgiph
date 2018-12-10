package com.navdroid.frshgiph.ui

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.navdroid.frshgiph.R
import com.navdroid.frshgiph.databinding.ItemGifLayoutBinding
import com.navdroid.frshgiph.model.Data
import java.util.ArrayList
import android.view.View.VISIBLE
import com.navdroid.frshgiph.Utils
import hari.bounceview.BounceView


class GifAdapter(private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<GifAdapter.ViewHolder>() {
    private var mGifs = ArrayList<Data>()
    private var mContext: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val itemBinding = DataBindingUtil.inflate<ItemGifLayoutBinding>(LayoutInflater.from(mContext), R.layout.item_gif_layout, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener(holder)
        holder.gif = mGifs[position]
        holder.itemBinding.imageButtonFav.setImageResource(if (holder.gif.isFavorite) R.drawable.ic_fav else R.drawable.ic_fav_empty)
        holder.itemBinding.imageButtonFav.visibility = GONE
        val options = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .centerCrop()
                .priority(Priority.HIGH)
        holder.itemBinding.progressBar.visibility = View.VISIBLE
        Utils.loadGif(mContext!!, options, holder.gif.imageUrl!!, holder.itemBinding.imageViewGif) {
            if (it) {
                holder.itemBinding.progressBar.visibility = GONE
                holder.itemBinding.imageButtonFav.visibility = VISIBLE
            }
        }
        holder.itemBinding.imageButtonFav.setOnClickListener {
            holder.itemBinding.imageButtonFav.setImageResource(if (holder.gif.isFavorite) R.drawable.ic_fav_empty else R.drawable.ic_fav)
            holder.gif.let { itemClickListener.favoriteButtonClicked(it) }
        }

        holder.itemBinding.root.setOnClickListener {
            Utils.previewImage(mContext!!, holder.gif)
        }

        BounceView.addAnimTo(holder.itemBinding.imageButtonFav)
                .setScaleForPopOutAnim(1.4f, 1.4f)
    }

    fun addAll(gifs: MutableList<Data>) {
        this.mGifs.addAll(gifs)
        notifyDataSetChanged()
    }

    fun clear() {
        mGifs.clear()
        notifyDataSetChanged()

    }

    inner class ViewHolder(var itemBinding: ItemGifLayoutBinding) : RecyclerView.ViewHolder(itemBinding.getRoot()), View.OnClickListener {
        lateinit var gif: Data
        override fun onClick(view: View) {

        }
    }
    override fun getItemCount()= mGifs.size

    interface ItemClickListener {
        fun favoriteButtonClicked(gif: Data)
    }
}

