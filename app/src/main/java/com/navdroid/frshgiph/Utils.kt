package com.navdroid.frshgiph

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.support.design.widget.Snackbar
import android.view.ViewGroup
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.navdroid.frshgiph.model.Data


object Utils {

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected
    }

    fun showSnackBar(activity: Activity, msg: String) {
        val snackbar = Snackbar.make(activity.findViewById<ViewGroup>(android.R.id.content), msg, Snackbar.LENGTH_LONG)
        snackbar.setActionTextColor(Color.WHITE)
        snackbar.show()
    }

    fun loadGif(mContext: Context, options: RequestOptions, url: String, imageView: ImageView, passedMethod: ((isSuccess: Boolean) -> Unit)) {
        Glide.with(mContext)
                .asGif()
                .listener(object : RequestListener<GifDrawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstResource: Boolean): Boolean {
                        passedMethod(false)
                        return false;
                    }

                    override fun onResourceReady(resource: GifDrawable?, model: Any?, target: Target<GifDrawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        passedMethod(true)
                        return false;
                    }
                })
                .apply(options)
                .load(url)
                .into(imageView)
    }

    fun hideKeyboard(view: View) {
        if(view==null)return
        val imm = (view.context).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun previewImage(mContext: Context, gif: Data) {
        if(mContext==null)return
        val view = LayoutInflater.from(mContext).inflate(R.layout.quick_view, null)
        val imageView = view.findViewById<ImageView>(R.id.imageViewGifPreview)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        val options = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .priority(Priority.HIGH)
        loadGif(mContext, options, gif.imageUrl!!, imageView) {
            if (it) progressBar.visibility = View.GONE
        }
        val builder = Dialog(mContext, R.style.mydialog)
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
        builder.setCanceledOnTouchOutside(true)
        builder.window?.setBackgroundDrawable(
                ColorDrawable(Color.parseColor("#bf000000")))
        builder.setContentView(view)
        builder.show()

        view.setOnClickListener {
            builder.dismiss()
        }
    }
}