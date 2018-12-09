package com.navdroid.frshgiph.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.navdroid.frshgiph.R
import com.navdroid.frshgiph.model.Data
import com.navdroid.frshgiph.viewmodel.MainViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_favourite.*


class FavouriteFragment : Fragment(), GifAdapter.ItemClickListener {


    lateinit var viewModel: MainViewModel
    private var isloading: Boolean = false
    private lateinit var mAdapter: GifAdapter
    private lateinit var mLayoutManager: GridLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favourite, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AndroidSupportInjection.inject(this)
        initList()
        setUpViewModel()
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProviders.of((activity as MainActivity), (activity as MainActivity).viewModelFactory).get(MainViewModel::class.java)
        isloading = true
        viewModel.getFavGifs()
        viewModel.mFavGifs.observe(this, Observer {
            if (it == null || it.isEmpty())
                textViewEmpty.visibility = View.VISIBLE
            else
                textViewEmpty.visibility = View.GONE

            isloading = false
            mAdapter.clear()
            mAdapter.addAll(it!!, true)
        })
    }

    private fun initList() {
        mLayoutManager = GridLayoutManager(activity, 2)
        mAdapter = GifAdapter(this)
        recyclerViewGif.layoutManager = mLayoutManager
        recyclerViewGif.adapter = mAdapter
    }

    override fun favoriteButtonClicked(gif: Data) {
        viewModel.updateFavGif(gif)
    }

    override fun itemClicked(gif: Data, view: View) {
    }
}
