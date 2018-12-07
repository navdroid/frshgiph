package com.navdroid.frshgiph.ui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.navdroid.frshgiph.R


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TrendingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }
}
