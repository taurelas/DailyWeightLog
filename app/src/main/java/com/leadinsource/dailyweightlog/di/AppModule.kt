package com.leadinsource.dailyweightlog.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.room.Room
import com.leadinsource.dailyweightlog.db.WeightDatabase
import com.leadinsource.dailyweightlog.ui.main.MainActivityViewModel
import com.leadinsource.dailyweightlog.ui.main.MainActivityViewModel_Factory

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
    fun provideActivityViewModel(db: WeightDatabase): MainActivityViewModel {
        return MainActivityViewModel_Factory.newInstance(db)
    }

}
