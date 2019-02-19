package com.smartlook.consentsdksample

import android.app.Application
import com.smartlook.consentsdk.ConsentSDK

class App: Application() {

    companion object {
        lateinit var consentSDK: ConsentSDK
    }

    override fun onCreate() {
        super.onCreate()

        consentSDK = ConsentSDK(applicationContext)
    }
}