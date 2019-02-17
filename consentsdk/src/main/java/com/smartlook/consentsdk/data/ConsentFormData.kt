package com.smartlook.consentsdk.data

import android.os.Bundle
import java.io.Serializable

data class ConsentFormData(val titleText: String,
                           val descriptionText: String,
                           val confirmButtonText: String,
                           val consentFormItems: Array<ConsentFormItem>) : Serializable {

    companion object {
        private const val CONSENT_EXTRA = "CONSENT_EXTRA"

        fun constructFromBundle(bundle: Bundle?): ConsentFormData? {
            return if (bundle != null && bundle.containsKey(CONSENT_EXTRA)) {
                bundle.getSerializable(CONSENT_EXTRA) as ConsentFormData
            } else {
                null
            }
        }
    }

    fun createBundle() = Bundle().apply { putSerializable(CONSENT_EXTRA, this@ConsentFormData) }
}