package com.smartlook.consentsdk.data

import android.os.Bundle
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ConsentFormData(val titleText: ConsentText,
                           val descriptionText: ConsentText,
                           val confirmButtonText: ConsentText,
                           val consentFormItems: Array<ConsentFormItem>) : Parcelable {

    companion object {
        private const val CONSENT_EXTRA = "CONSENT_EXTRA"

        fun constructFromBundle(bundle: Bundle?): ConsentFormData? {
            return if (bundle != null && bundle.containsKey(CONSENT_EXTRA)) {
                bundle.getParcelable(CONSENT_EXTRA) as ConsentFormData?
            } else {
                null
            }
        }
    }

    fun createBundle() = Bundle().apply { putParcelable(CONSENT_EXTRA, this@ConsentFormData) }
}