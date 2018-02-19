package com.leadinsource.dailyweightlog.app;

import android.app.Application;

import com.leadinsource.dailyweightlog.di.AppComponent;
import com.leadinsource.dailyweightlog.di.AppModule;
import com.leadinsource.dailyweightlog.di.DaggerAppComponent;

public class DWLApplication extends Application /* implements HasActivityInjector*/ {

    /*@Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;*/

    private static DWLApplication app;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;

        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(getApplicationContext()))
                .build();
        /*DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this);*/
    }


    /*@Override
    public AndroidInjector<Activity> activityInjector() {
        return null;
    }*/

    public static DWLApplication app() {
        return app;
    }

    public AppComponent appComponent() {
        return appComponent;
    }
}
