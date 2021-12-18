package com.olibei.dailyweightlog.di

import com.olibei.dailyweightlog.HistoryActivity
import com.olibei.dailyweightlog.ui.main.MainActivity
import com.olibei.dailyweightlog.SettingsActivity
import com.olibei.dailyweightlog.ui.main.PreviousFragment
import com.olibei.dailyweightlog.ui.main.input.InputFragment
//import com.olibei.dailyweightlog.SettingsFragment
import com.olibei.dailyweightlog.ui.welcome.WelcomeActivity

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
    fun inject(previousFragment: PreviousFragment)
}
