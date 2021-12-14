package com.leadinsource.dailyweightlog.ui.main

import android.util.Log
import androidx.lifecycle.*
import com.leadinsource.dailyweightlog.data.WeightRepository
import com.leadinsource.dailyweightlog.db.Weight
import com.leadinsource.dailyweightlog.db.WeightDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * Created by Matt on 23/02/2018.
 * ViewModel shared with the MainActivity's fragments.
 */
class MainActivityViewModel @Inject constructor(
    val repo: WeightRepository
) : ViewModel() {

    private var weights: MutableLiveData<Weight> = MutableLiveData()

    val weightEntered: MutableLiveData<String> = MutableLiveData("")

    val submitButtonEnabled = weightEntered.map { it.isNullOrBlank().not() }

    fun undo() {

    }

    fun saveData(weight: Float, fatPc: Float? = null) {
        viewModelScope.launch {
            Log.d("DWL", "Saving weight $weight")
            val weightData = Weight(date = Date(), weightInKg = weight, fatPc = fatPc)
            repo.addWeight(weightData)
        }

        viewModelScope.launch {
            Log.d("DWL", "data read: ${repo.getAll()}")
        }

        //TODO next use Google Drive to save it to Drive if logged in save as MD to share with Obisidian
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
