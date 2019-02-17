package com.smartlook.consentsdk

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.support.v4.app.FragmentActivity
import com.smartlook.consentsdk.data.ConsentFormData
import com.smartlook.consentsdk.helpers.SharedPreferencesHelper
import com.smartlook.consentsdk.listeners.ConsentResultListener
import com.smartlook.consentsdk.ui.consent.activity.ConsentActivity
import com.smartlook.consentsdk.ui.consent.dialog.ConsentDialog
import com.smartlook.consentsdk.ui.consent.dialog.ConsentDialogFragment

class ConsentSDK(context: Context) : ContextWrapper(context) {

    companion object {
        private const val CONSENT_RESULT_STORED = "consent_result_stored"
    }

    private val sharedPreferences = SharedPreferencesHelper(this)

    fun loadConsentResult(key: String) = sharedPreferences.loadBoolean(key)

    fun saveConsentResult(key: String, grantResult: Boolean) = sharedPreferences.saveBoolean(key, grantResult)

    fun isConsentResultStored() = sharedPreferences.loadBoolean(CONSENT_RESULT_STORED)

    fun setConsentResultStored() = sharedPreferences.saveBoolean(CONSENT_RESULT_STORED, true)

    fun showConsentFormDialog(consentFormData: ConsentFormData, consentResultListener: ConsentResultListener) =
            ConsentDialog(this, consentFormData, consentResultListener).show()


    fun showConsentFormDialogFragment(activity: FragmentActivity, consentFormData: ConsentFormData) {
        ConsentDialogFragment.show(activity, consentFormData)
    }

    fun startConsentFormActivity(activity: Activity, consentFormData: ConsentFormData, requestCode: Int) =
            ConsentActivity.start(activity, consentFormData, requestCode)

}