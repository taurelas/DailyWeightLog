package com.leadinsource.dailyweightlog.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.leadinsource.dailyweightlog.db.Weight

/**
 * Created by Matt on 23/02/2018.
 */

class MainActivityViewModel : ViewModel() {

    private var weights: MutableLiveData<Weight> = MutableLiveData()

    var weightEntered: MutableLiveData<Boolean>

    init {
        weightEntered = MutableLiveData()
        weightEntered.postValue(false)
    }


    fun getWeights(): LiveData<Weight> {

        return weights
    }


    /**
     * This is observed by the view, so it can change the UI accordingly
     * @return
     */

    fun getWeightEntered(): LiveData<Boolean> {

        return weightEntered
    }

    fun undo() {

    }

    fun addData(weight: Float, fatPc: Float) {

        //TODO use ROOM to save data

        //setting this will trigger automatic update of UI
        weightEntered.value = true
    }
}
