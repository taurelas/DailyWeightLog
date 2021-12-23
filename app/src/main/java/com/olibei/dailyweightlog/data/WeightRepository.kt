package com.olibei.dailyweightlog.data

import com.olibei.dailyweightlog.db.Weight
import com.olibei.dailyweightlog.db.WeightDatabase
import javax.inject.Inject

class WeightRepository @Inject constructor(
    val db: WeightDatabase,
    val fileRepository: FileRepository,
    val dateConverter: DateConverter,
) {
    suspend fun addWeight(weight: Weight) {
        db.weightDao().insert(weight)
    }

    suspend fun getAll(): List<Weight> {
        return db.weightDao().getAll()
    }

    suspend fun exportTextFile() {
        val data = db.weightDao().getAll()
        val stringBuilder = StringBuilder()

        for(weight: Weight in data.reversed()) {
            stringBuilder.append(
                "${dateConverter.getString(weight.date)} ${weight.weightInKg} ${weight.fatPc ?: ""}\n")
        }

        fileRepository.writeToFile(stringBuilder.toString())
    }
}