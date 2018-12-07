package com.navdroid.frshgiph.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import com.navdroid.frshgiph.MainApplication
import com.navdroid.frshgiph.R
import com.navdroid.frshgiph.model.ApiEmptyResponse
import com.navdroid.frshgiph.model.ApiErrorResponse
import com.navdroid.frshgiph.model.ApiIsLoading
import com.navdroid.frshgiph.model.ApiSuccessResponse
import com.navdroid.frshgiph.network.ApiEndPoints
import com.navdroid.frshgiph.repos.GiphyRepo
import com.navdroid.frshgiph.viewmodel.MainViewModel
import com.navdroid.frshgiph.viewmodel.MainViewModelFactory
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    lateinit var viewModel: MainViewModel

    override fun onPageScrollStateChanged(p0: Int) {
    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
    }

    override fun onPageSelected(p0: Int) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
        setUpViewModel()
    }

    private fun initUI() {

        viewPager.addOnPageChangeListener(this)
        viewPager.adapter = PagerAdapter(this, supportFragmentManager)
        slidingTabs.setupWithViewPager(viewPager)
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        viewModel.getTrend().observe(this, Observer {
           when(it)
           {
               is ApiIsLoading -> {
                   Log.d("TAG","LOADING")
                   null
               }
               is ApiSuccessResponse -> {
                   val responseBody = it.body
                   Log.d("TAG",it.body.toString())

               }
               is ApiEmptyResponse<*> -> {
                   Log.d("TAG","EMPTY")

                   null
               }
               is ApiErrorResponse<*> -> {
                   Log.d("TAG","EROORe")

                   null
               }
               else -> null
           }
        })
    }

    class PagerAdapter(val context: Context, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        private val fragmentList = ArrayList<Fragment>()

        init {

            fragmentList.add(TrendingFragment())
            fragmentList.add(FavouriteFragment())
        }

        override fun getItem(pos: Int): Fragment {
            return fragmentList[pos]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {

            return when (position) {
                0 -> context.getString(R.string.trending)
                1 -> context.getString(R.string.favourites)
                else -> ""
            }
        }
    }
}
