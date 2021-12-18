package com.olibei.dailyweightlog.data

import com.olibei.dailyweightlog.db.Weight
import com.olibei.dailyweightlog.db.WeightDatabase
import javax.inject.Inject

class WeightRepository @Inject constructor(
    val db: WeightDatabase
) {
    suspend fun addWeight(weight: Weight) {
        db.weightDao().insert(weight)
    }

    suspend fun getAll(): List<Weight> {
        return db.weightDao().getAll()
    }
}