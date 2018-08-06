package com.leadinsource.dailyweightlog.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import java.util.List;

/**
 * Single point of truth
 */

public class DataRepository {

    // singleton pattern
    private static DataRepository instance;
    // dependency injected via constructor
    private final WeightDatabase database;

    // not sure why we use MediatorLiveData and not the other
    private MediatorLiveData<List<Weight>> observableWeights;

    private DataRepository(final WeightDatabase database) {
        this.database = database;
    }


    /**
     * Implementing a singleton pattern
     */
    public static DataRepository getInstance(final WeightDatabase database) {
        if(instance==null) {
            synchronized (DataRepository.class) {
                if (instance==null) {
                    instance = new DataRepository(database);
                }
            }
        }

        return instance;
    }

    /**
     * Get the list of weights from the database and get notified when the data changes.
     *
     * @return ??
     */

    public LiveData<List<Weight>> getWeights() {
        return observableWeights;
    }

    public LiveData<Weight> loadWeight(final int weightId) {
        return database.weightDao().getSingleWeight(weightId);
    }

}
