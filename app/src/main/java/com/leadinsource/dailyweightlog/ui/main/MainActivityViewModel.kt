package com.leadinsource.dailyweightlog.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.leadinsource.dailyweightlog.db.Weight

/**
 * Created by Matt on 23/02/2018.
 * ViewModel shared with the MainActivity's fragments.
 */
class MainActivityViewModel : ViewModel() {

    private var weights: MutableLiveData<Weight> = MutableLiveData()

    val weightEntered: MutableLiveData<String> = MutableLiveData("")

    val submitButtonEnabled = weightEntered.map { it.isNullOrBlank().not() }

    fun undo() {

    }

    fun saveData(weight: Float, fatPc: Float? = null) {

        //TODO use ROOM to save data or better, save it to Drive if logged in

        Log.d("DWL", "Saving weight $weight")
    }

    fun onSubmitClicked() {
        val weight = weightEntered.value?.toFloatOrNull()
        checkNotNull(weight) {
            "UI should prevent this method to run if the weight value is null and should not allow" +
                    "non-float values"
        }
        Log.d("DWL", "Rcvd click, got value ${weightEntered.value}")
        saveData(weight)
        weightEntered.value = ""
    }
}
