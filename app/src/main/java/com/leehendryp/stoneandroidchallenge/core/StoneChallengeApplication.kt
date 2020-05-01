package com.leehendryp.stoneandroidchallenge.core

import android.app.Application
import com.leehendryp.stoneandroidchallenge.core.inversionofcontrol.AppComponent
import com.leehendryp.stoneandroidchallenge.core.inversionofcontrol.DaggerAppComponent

class StoneChallengeApplication : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}