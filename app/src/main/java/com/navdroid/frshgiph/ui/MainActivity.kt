package com.navdroid.frshgiph.ui

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import com.navdroid.frshgiph.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {
    override fun onPageScrollStateChanged(p0: Int) {
    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
    }

    override fun onPageSelected(p0: Int) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()
    }

    private fun initUI() {

        viewPager.addOnPageChangeListener(this)
        viewPager.adapter = PagerAdapter(this, supportFragmentManager)
        slidingTabs.setupWithViewPager(viewPager)
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
