package br.com.fbsantos.baseapp

import android.app.Application
import br.com.fbsantos.baseapp.util.RecaptchaHelper
import br.com.fbsantos.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        RecaptchaHelper.initialize(this)

        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}