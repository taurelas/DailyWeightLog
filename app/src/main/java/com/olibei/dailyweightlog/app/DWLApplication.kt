package com.olibei.dailyweightlog.app

import android.app.Application

import com.olibei.dailyweightlog.di.AppComponent
import com.olibei.dailyweightlog.di.AppModule
import com.olibei.dailyweightlog.di.DaggerAppComponent

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
