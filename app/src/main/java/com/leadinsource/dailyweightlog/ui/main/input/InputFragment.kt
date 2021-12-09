package com.leadinsource.dailyweightlog.ui.main.input

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels

import com.leadinsource.dailyweightlog.R
import com.leadinsource.dailyweightlog.databinding.FragmentInputBinding
import com.leadinsource.dailyweightlog.ui.DataBindingFragment
import com.leadinsource.dailyweightlog.ui.main.MainActivityViewModel

/**
 * Standard input fragment, we need to have a working app.
 */
class InputFragment : DataBindingFragment() {

    //viewModel shared with activity
    private val viewModel: MainActivityViewModel by activityViewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ViewDataBinding = FragmentInputBinding.inflate(inflater, container, false).also {
        it.viewModel = viewModel
    }
}
