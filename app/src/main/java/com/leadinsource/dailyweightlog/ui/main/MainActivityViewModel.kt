package com.leadinsource.dailyweightlog.ui.main

import android.text.format.DateFormat
import android.util.Log
import androidx.lifecycle.*
import com.leadinsource.dailyweightlog.data.WeightRepository
import com.leadinsource.dailyweightlog.db.Weight
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

    private var weights: MutableLiveData<List<Weight>> = MutableLiveData(emptyList())

    val data: LiveData<List<ItemViewModel>>
        get() = _data
    private val _data = MutableLiveData<List<ItemViewModel>>(emptyList())

    val weightEntered: MutableLiveData<String> = MutableLiveData("")

    val submitButtonEnabled = weightEntered.map { it.isNullOrBlank().not() }

   init {
       loadData()
   }

    private fun loadData() {
        viewModelScope.launch {
            val data = repo.getAll().reversed()
            weights.postValue(data)
            Log.d("DWL", "data read: ${data}")

            val viewData = createViewData(data)
            _data.postValue(viewData)
        }
    }

    private fun createViewData(data: List<Weight>): List<DefaultItemViewModel> {
        return data.map {
            DefaultItemViewModel(it.date, it.weightInKg.toString(), it.fatPc?.toString() ?: "")
        }
    }

    fun saveData(weight: Float, fatPc: Float? = null) {
        viewModelScope.launch {
            Log.d("DWL", "Saving weight $weight")
            val weightData = Weight(date = Date(), weightInKg = weight, fatPc = fatPc)
            repo.addWeight(weightData)
            loadData()
        }

        // TODO next use Google Drive to save it to Drive if logged in save as MD to share with Obisidian
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
