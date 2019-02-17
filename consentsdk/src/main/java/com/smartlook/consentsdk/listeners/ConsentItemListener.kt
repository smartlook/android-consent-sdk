package com.smartlook.consentsdk.listeners

interface ConsentItemListener {
    fun onConsentChange(itemIndex: Int, consent: Boolean)
}