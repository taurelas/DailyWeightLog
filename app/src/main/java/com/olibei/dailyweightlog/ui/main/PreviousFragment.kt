package com.olibei.dailyweightlog.ui.main


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup

import com.olibei.dailyweightlog.app.DWLApplication
import com.olibei.dailyweightlog.databinding.FragmentPreviousBinding
import com.olibei.dailyweightlog.ui.DataBindingFragment
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class PreviousFragment : DataBindingFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DWLApplication.app().appComponent().inject(this)
    }

    //viewModel shared with activity
    @Inject
    lateinit var viewModel: MainActivityViewModel

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentPreviousBinding.inflate(inflater, container, false).also {
        it.viewModel = viewModel
    }

}// Required empty public constructor
