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
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_blank.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.navdroid.frshgiph.Utils


class TrendingFragment : Fragment(), GifAdapter.ItemClickListener {

    lateinit var viewModel: MainViewModel
    private var isLoadMore: Boolean = false
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
        progressBar.isIndeterminate = true
        progressBar.visibility = View.VISIBLE

        viewModel.mGifs.observe(this, Observer {

            if (it == null || it.isEmpty()) {
                textViewEmpty.visibility = View.VISIBLE
                if (!Utils.isNetworkAvailable(activity!!))
                    textViewEmpty.text = getString(R.string.internet_check)
                else
                    textViewEmpty.text = getString(R.string.out_of_gifs_n_please_use_a_new_keywords)
            } else
                textViewEmpty.visibility = View.GONE

            isloading = false
            progressBar.visibility = View.GONE
            mAdapter.clear()
            mAdapter.addAll(it!!)
        })

        viewModel.error.observe(this, Observer {
            if (it != null) {
                progressBar.visibility = View.GONE
                Log.e("TAG", it)

                Utils.showSnackBar(activity!!,
                        if (!Utils.isNetworkAvailable(activity!!)) getString(R.string.internet_check)
                        else getString(R.string.common_error_msg))

            }
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

                if (!recyclerView.canScrollVertically(1) && !isloading && Utils.isNetworkAvailable(activity!!)) {
                    isLoadMore = true
                    isloading = true
                    viewModel.getGifs(editTextSearch.text.toString(), isLoadMore)
                }
            }
        })

        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                textViewEmpty.visibility = View.GONE
                isLoadMore = false
                isloading = true
                mAdapter.clear()
                progressBar.visibility = View.VISIBLE
                viewModel.getGifs(charSequence.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        editTextSearch.setOnEditorActionListener(TextView.OnEditorActionListener { view, actionId, p2 ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        Utils.hideKeyboard(view)
                        return@OnEditorActionListener true
                    }
                    false
                })
    }

    override fun favoriteButtonClicked(gif: Data) {
        viewModel.updateFavGif(gif)
    }

}
