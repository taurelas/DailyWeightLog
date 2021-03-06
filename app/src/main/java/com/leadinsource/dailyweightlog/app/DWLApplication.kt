package com.leadinsource.dailyweightlog.app

import android.app.Application

import com.leadinsource.dailyweightlog.di.AppComponent
import com.leadinsource.dailyweightlog.di.AppModule
import com.leadinsource.dailyweightlog.di.DaggerAppComponent

class DWLApplication : Application() {
    private val appComponent: AppComponent by lazy {
        DaggerAppComponent
                .builder()
                .appModule(AppModule(applicationContext))
                .build()
    }

    override fun onCreate() {
        super.onCreate()

        app = this

    }

    fun appComponent(): AppComponent {
        return appComponent
    }

    companion object {

        private lateinit var app: DWLApplication

        fun app(): DWLApplication {
            return app
        }
    }
}
