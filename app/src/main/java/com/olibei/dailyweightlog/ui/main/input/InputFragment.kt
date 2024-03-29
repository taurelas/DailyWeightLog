package com.olibei.dailyweightlog.ui.main.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.olibei.dailyweightlog.app.DWLApplication

import com.olibei.dailyweightlog.databinding.FragmentInputBinding
import com.olibei.dailyweightlog.ui.DataBindingFragment
import com.olibei.dailyweightlog.ui.main.MainActivityViewModel
import javax.inject.Inject

/**
 * Standard input fragment, we need to have a working app.
 */
class InputFragment : DataBindingFragment() {

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
    ): ViewDataBinding = FragmentInputBinding.inflate(inflater, container, false).also {
        it.viewModel = viewModel
    }
}
