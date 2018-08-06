package com.leadinsource.dailyweightlog.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.leadinsource.dailyweightlog.db.DataRepository;
import com.leadinsource.dailyweightlog.db.Weight;

/**
 * Created by Matt on 23/02/2018.
 */

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<Weight> weights;
    private DataRepository dataRepo;

    private MutableLiveData<Boolean> weightEntered;

    public LiveData<Weight> getWeights() {
        if(weights==null) {
            weights = new MutableLiveData<>();
        }

        return weights;
    }


    /**
     * This is observed by the view, so it can change the UI accordingly
     * @return
     */

    public LiveData<Boolean> getWeightEntered() {
        if(weightEntered==null) {
            weightEntered = new MutableLiveData<>();
        }

        return weightEntered;
    }

    public void undo() {

    }

    public void addData(float weight, float fatPc) {

        //TODO use ROOM to save data

        //setting this will trigger automatic update of UI
        weightEntered.setValue(true);
    }
}
