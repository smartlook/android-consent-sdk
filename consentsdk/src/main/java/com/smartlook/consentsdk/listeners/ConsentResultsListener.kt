package com.smartlook.consentsdk.listeners

interface ConsentResultsListener {
    fun onConsentResults(consentResults: HashMap<String, Boolean>)
}