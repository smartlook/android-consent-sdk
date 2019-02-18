package com.smartlook.consentsdk.listeners

interface ConsentResultListener {
    fun onConsentResult(consentResults: HashMap<String, Boolean>)
}