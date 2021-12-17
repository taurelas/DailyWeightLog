package com.leadinsource.dailyweightlog.data

import com.leadinsource.dailyweightlog.db.Weight
import com.leadinsource.dailyweightlog.db.WeightDatabase
import java.util.*
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