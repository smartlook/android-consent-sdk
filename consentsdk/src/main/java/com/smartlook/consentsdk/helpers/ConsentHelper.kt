package com.smartlook.consentsdk.helpers

import android.os.Bundle

object ConsentHelper {

    const val CONSENT_RESULTS_EXTRA = "CONSENT_RESULTS_EXTRA"

    fun storeConsentResults(outState: Bundle, consentResults: HashMap<String, Boolean>) {
        outState.putSerializable(CONSENT_RESULTS_EXTRA, consentResults)
    }

    fun restoreConsentResults(inState: Bundle?): HashMap<String, Boolean>? {
        return if (inState == null || !inState.containsKey(CONSENT_RESULTS_EXTRA)) {
            null
        } else {
            inState.getSerializable(CONSENT_RESULTS_EXTRA) as HashMap<String, Boolean>
        }
    }

}