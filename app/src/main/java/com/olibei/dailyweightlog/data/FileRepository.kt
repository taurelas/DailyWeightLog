package com.olibei.dailyweightlog.data

import android.content.Context
import java.io.File
import javax.inject.Inject

private const val FILENAME = "weights.txt"

class FileRepository @Inject constructor(val context: Context) {
    fun writeToFile(data: String) {
        val file = File(context.filesDir, FILENAME)
        file.writeText(data)
    }

    fun readFile(): String {
        val file = File(context.filesDir, FILENAME)
        return file.readText()
    }
}