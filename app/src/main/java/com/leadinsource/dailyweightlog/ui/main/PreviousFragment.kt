package com.leadinsource.dailyweightlog.ui.main


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.leadinsource.dailyweightlog.R


/**
 * A simple [Fragment] subclass.
 */
class PreviousFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_previous, container, false)
    }

}// Required empty public constructor