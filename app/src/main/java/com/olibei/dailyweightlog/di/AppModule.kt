package com.olibei.dailyweightlog.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.room.Room
import com.olibei.dailyweightlog.data.DateConverter
import com.olibei.dailyweightlog.data.FileRepository
import com.olibei.dailyweightlog.data.WeightRepository
import com.olibei.dailyweightlog.db.WeightDatabase
import com.olibei.dailyweightlog.ui.main.MainActivityViewModel
import com.olibei.dailyweightlog.ui.main.MainActivityViewModel_Factory

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

/**
 * Dagger 2 module
 */
@Module
class AppModule(private val context: Context) {

    @Singleton
    @Provides
    fun provideContext(): Context {
        return context
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Singleton
    @Provides
    fun provideDatabase(context: Context): WeightDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            WeightDatabase::class.java, "weights"
        ).build()
    }

    @Singleton
    @Provides
    fun provideActivityViewModel(repository: WeightRepository): MainActivityViewModel {
        return MainActivityViewModel_Factory.newInstance(repository)
    }

    @Singleton
    @Provides
    fun provideWeightRepository(db: WeightDatabase, fileRepository: FileRepository, dateConverter: DateConverter): WeightRepository {
        return WeightRepository(db, fileRepository, dateConverter)
    }

    @Singleton
    fun provideFileRepository(context: Context): FileRepository {
        return FileRepository(context)
    }

    @Singleton
    fun provideDateConverter(context: Context): DateConverter {
        return DateConverter(context)
    }

}
