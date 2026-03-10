package br.com.dev360.simpleapplicationnetworking.app

import android.app.Application
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.di.AppContainer
import br.com.dev360.simpleapplicationnetworking.feature.characterlist.di.DefaultAppContainer

class SimpleApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}