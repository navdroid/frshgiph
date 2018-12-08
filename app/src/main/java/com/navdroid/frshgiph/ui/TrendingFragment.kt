package com.navdroid.frshgiph.ui


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.navdroid.frshgiph.R
import com.navdroid.frshgiph.model.*
import com.navdroid.frshgiph.viewmodel.MainViewModel
import com.navdroid.frshgiph.viewmodel.MainViewModelFactory
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_blank.*
import javax.inject.Inject


class TrendingFragment : Fragment(), GifAdapter.ItemClickListener {

    lateinit var viewModel: MainViewModel
    private var isNext: Boolean = false
    private var isloading: Boolean = false
    private lateinit var mAdapter: GifAdapter
    private lateinit var mLayoutManager: GridLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AndroidSupportInjection.inject(this)
        initList()
        setUpViewModel()
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProviders.of((activity as MainActivity), (activity as MainActivity).viewModelFactory).get(MainViewModel::class.java)
        viewModel.getGifs()
        isloading = true
        viewModel.mGifs.observe(this, Observer {
            isloading = false
            mAdapter.addAll(it!!, !isNext)
        })
    }

    private fun initList() {
        mLayoutManager = GridLayoutManager(activity, 1)
        mAdapter = GifAdapter(this)
        recyclerViewGif.layoutManager = mLayoutManager
        recyclerViewGif.adapter = mAdapter
        recyclerViewGif.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1) && !isloading) {
                    isNext = true
                    isloading = true
                    viewModel.getGifs(editTextSearch.text.toString(), isNext)
                }
            }
        })

        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                isNext = false
                isloading = true
                viewModel.getGifs(charSequence.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    override fun favoriteButtonClicked(gif: Data) {
        viewModel.updateFavGif(gif)
    }

    override fun itemClicked(gif: Data) {
    }


}
