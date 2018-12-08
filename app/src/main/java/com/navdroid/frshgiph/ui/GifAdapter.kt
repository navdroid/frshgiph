package com.navdroid.frshgiph.ui

import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.navdroid.frshgiph.R
import com.navdroid.frshgiph.databinding.ItemGifLayoutBinding
import com.navdroid.frshgiph.model.Data
import com.orm.SugarRecord
import java.util.ArrayList


class GifAdapter(val itemClickListener: ItemClickListener) : RecyclerView.Adapter<GifAdapter.ViewHolder>() {
    private var mGifs: MutableList<Data> = arrayListOf()
    private var context: Context? = null

    fun addAll(gifs: MutableList<Data>, isClear: Boolean = false) {
        var size = 0
        size = this.mGifs!!.size
        if (isClear) mGifs?.clear()
        this.mGifs?.addAll(gifs)

        if (isClear)
            notifyDataSetChanged()
        else
            notifyItemRangeInserted(size, this.mGifs!!.size)

    }

    inner class ViewHolder(var itemBinding: ItemGifLayoutBinding) : RecyclerView.ViewHolder(itemBinding.getRoot()), View.OnClickListener {


        lateinit var gif: Data

        override fun onClick(view: View) {

        }
    }

    init {
        this.mGifs = ArrayList<Data>()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val itemBinding = DataBindingUtil.inflate<ItemGifLayoutBinding>(LayoutInflater.from(context), R.layout.item_gif_layout, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener(holder)
        holder.gif = mGifs[position]
        val saveGif = SugarRecord.find(Data::class.java, "uid = ?", holder.gif.uid)
        holder.itemBinding.imageButtonFav.setImageResource(if (saveGif.isNotEmpty()) R.drawable.ic_fav else R.drawable.ic_fav_empty)

        val options = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .centerCrop()
                .priority(Priority.HIGH)
        holder.itemBinding.progressBar.visibility = View.VISIBLE
        Glide.with(context!!)
                .asGif()
                .listener(object : RequestListener<GifDrawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstResource: Boolean): Boolean {
                        return false;
                    }

                    override fun onResourceReady(resource: GifDrawable?, model: Any?, target: Target<GifDrawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        holder.itemBinding.progressBar.visibility = GONE
                        return false;
                    }

                })
                .apply(options)
                .load(holder.gif.imageUrl)
                .into(holder.itemBinding.imageViewGif)

        holder.itemBinding.imageButtonFav.setOnClickListener {
            holder.itemBinding.imageButtonFav.setImageResource(if (saveGif.isNotEmpty()) R.drawable.ic_fav_empty else R.drawable.ic_fav)
            holder.gif.let { itemClickListener.favoriteButtonClicked(it) }
        }

    }


    override fun getItemCount(): Int {

        return mGifs!!.size
    }

    interface ItemClickListener {
        fun favoriteButtonClicked(gif: Data)
        fun itemClicked(gif: Data)
    }
}

