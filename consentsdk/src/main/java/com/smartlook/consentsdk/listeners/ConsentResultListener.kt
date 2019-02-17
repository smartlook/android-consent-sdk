package com.smartlook.consentsdk.listeners

interface ConsentResultListener {

    //todo Meaby return hash map ?
    fun onConsentResult(consentKeys: Array<String>, grantResults: BooleanArray)
}