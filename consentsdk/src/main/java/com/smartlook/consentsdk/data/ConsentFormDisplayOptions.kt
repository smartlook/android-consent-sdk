package com.smartlook.consentsdk.data

import android.os.Bundle
import java.io.Serializable

data class ConsentFormDisplayOptions(
    val consentFormDescriptionScrollingOnly: Boolean = true
) : Serializable {

    companion object {
        private const val CONSENT_DISPLAY_OPTIONS_EXTRA = "CONSENT_DISPLAY_OPTIONS_EXTRA"

        fun constructFromBundle(bundle: Bundle?): ConsentFormDisplayOptions? {
            return if (bundle != null && bundle.containsKey(CONSENT_DISPLAY_OPTIONS_EXTRA)) {
                bundle.getSerializable(CONSENT_DISPLAY_OPTIONS_EXTRA) as ConsentFormDisplayOptions
            } else {
                null
            }
        }
    }

    fun saveToBundle(bundle: Bundle = Bundle()) = bundle.apply { putSerializable(CONSENT_DISPLAY_OPTIONS_EXTRA, this@ConsentFormDisplayOptions) }
}