package com.leadinsource.dailyweightlog.di

import com.leadinsource.dailyweightlog.HistoryActivity
import com.leadinsource.dailyweightlog.ui.main.MainActivity
import com.leadinsource.dailyweightlog.SettingsActivity
import com.leadinsource.dailyweightlog.ui.main.input.InputFragment
//import com.leadinsource.dailyweightlog.SettingsFragment
import com.leadinsource.dailyweightlog.ui.welcome.WelcomeActivity

import javax.inject.Singleton

import dagger.Component

/**
 * Dagger 2 component
 */
@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

    fun inject(welcomeActivity: WelcomeActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(historyActivity: HistoryActivity)
    fun inject(settingsActivity: SettingsActivity)
   // fun inject(settingsFragment: SettingsFragment)
    fun inject(inputFragment: InputFragment)
}
