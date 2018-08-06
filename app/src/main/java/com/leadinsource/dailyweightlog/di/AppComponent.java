package com.leadinsource.dailyweightlog.di;

import com.leadinsource.dailyweightlog.HistoryActivity;
import com.leadinsource.dailyweightlog.ui.main.MainActivity;
import com.leadinsource.dailyweightlog.SettingsActivity;
import com.leadinsource.dailyweightlog.SettingsFragment;
import com.leadinsource.dailyweightlog.ui.welcome.WelcomeActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger 2 component
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(WelcomeActivity welcomeActivity);
    void inject(MainActivity mainActivity);
    void inject(HistoryActivity historyActivity);
    void inject(SettingsActivity settingsActivity);
    void inject(SettingsFragment settingsFragment);
}
