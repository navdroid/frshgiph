package com.navdroid.frshgiph.ui

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.navdroid.frshgiph.R
import com.navdroid.frshgiph.databinding.ItemGifLayoutBinding
import com.navdroid.frshgiph.model.Data
import java.util.ArrayList
import android.view.View.VISIBLE
import android.graphics.drawable.ColorDrawable
import android.view.Window.FEATURE_NO_TITLE
import android.R.attr.priority
import android.app.Dialog
import android.databinding.adapters.TextViewBindingAdapter.setText
import android.graphics.Color
import android.view.Window
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
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
            previewImage(holder.gif)
        }

        BounceView.addAnimTo(holder.itemBinding.imageButtonFav)
                .setScaleForPopOutAnim(1.4f, 1.4f)
    }

    fun addAll(gifs: MutableList<Data>, isClear: Boolean = false) {
        var size = 0
        size = this.mGifs.size
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


    override fun getItemCount(): Int {

        return mGifs.size
    }

    interface ItemClickListener {
        fun favoriteButtonClicked(gif: Data)
    }

    var builder: Dialog? = null
    fun previewImage(gif: Data) {
        val view = LayoutInflater.from(mContext).inflate(R.layout.quick_view, null)

        val imageView = view.findViewById<ImageView>(R.id.imageViewGifPreview)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        progressBar.visibility = View.VISIBLE
        val options = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.DATA)

                .priority(Priority.HIGH)
        Utils.loadGif(mContext!!, options, gif.imageUrl!!, imageView) {
            if (it)
                progressBar.visibility = View.GONE
        }

        view.setOnClickListener {
            builder?.dismiss()
        }

        builder = Dialog(mContext, R.style.mydialog)
        builder?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        builder?.setCanceledOnTouchOutside(true)
        builder?.window?.setBackgroundDrawable(
                ColorDrawable(Color.parseColor("#bf000000")))
        builder?.setContentView(view)
        builder?.show()
    }


}

