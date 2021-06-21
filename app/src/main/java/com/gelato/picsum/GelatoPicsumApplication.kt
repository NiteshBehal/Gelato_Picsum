package com.gelato.picsum

import android.app.Application
import com.gelato.picsum.di.ComponentFactory
import com.gelato.picsum.di.application.ApplicationComponent

class GelatoPicsumApplication : Application() {
    companion object {
        lateinit var applicationComponent: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        applicationComponent = ComponentFactory.createApplicationComponent(this)

    }
}