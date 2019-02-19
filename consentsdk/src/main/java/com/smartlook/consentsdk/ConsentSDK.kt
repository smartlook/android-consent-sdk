package com.smartlook.consentsdk

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.smartlook.consentsdk.data.ConsentFormData
import com.smartlook.consentsdk.helpers.ConsentHelper
import com.smartlook.consentsdk.helpers.SharedPreferencesHelper
import com.smartlook.consentsdk.listeners.ConsentResultsListener
import com.smartlook.consentsdk.ui.consent.activity.ConsentActivity
import com.smartlook.consentsdk.ui.consent.dialog.ConsentDialog
import com.smartlook.consentsdk.ui.consent.dialog.ConsentDialogFragment
import com.smartlook.consentsdk.ui.consent.fragment.ConsentFragment

class ConsentSDK(context: Context) : ContextWrapper(context) {

    companion object {
        private const val CONSENT_RESULT_STORED = "consent_result_stored"
    }

    private val sharedPreferences = SharedPreferencesHelper(this)

    /**
     * Load consent granting result from preferences.
     *
     * @param consentKey Unique key identifying consent.
     * @return TRUE if consent was granted, FALSE if it was refused and null if not defined.
     */
    fun loadConsentResult(consentKey: String): Boolean? {
        return if (areConsentResultsStored()) {
            sharedPreferences.loadBoolean(consentKey)
        } else {
            null
        }
    }

    /**
     * Save consent granting result to preferences.
     *
     * @param consentKey Unique key identifying consent.
     * @param grantResult TRUE if consent was granted, FALSE if it was refused.
     */
    fun saveConsentResult(consentKey: String, grantResult: Boolean) =
        sharedPreferences.saveBoolean(consentKey, grantResult)

    /**
     * Check if user has seen and successfully filled consent form.
     *
     * @return TRUE if user has seen and successfully filled consent form.
     */
    fun areConsentResultsStored() = sharedPreferences.loadBoolean(CONSENT_RESULT_STORED)

    /**
     * Store information that user filled consent form successfully.
     */
    fun setConsentResultsStored() = sharedPreferences.saveBoolean(CONSENT_RESULT_STORED, true)

    /**
     * Display consent form on Dialog. If you want to correctly persist on orientation change use
     * showConsentFormDialogFragment().
     *
     * @param activity Calling Activity reference.
     * @param consentFormData Data object containing all needed info display the form (@see ConsentFormData).
     * @param consentResultsListener Callback called on successful fill of consent form (@see ConsentResultsListener).
     */
    fun showConsentFormDialog(activity: Activity, consentFormData: ConsentFormData, consentResultsListener: ConsentResultsListener) =
        ConsentDialog(activity, consentFormData, consentResultsListener).show()

    /**
     * Display consent form on DialogFragment.
     *
     * @param activity Calling Activity reference. This Activity needs to implement ConsentResultsListener.
     * @param consentFormData Data object containing all needed info display the form (@see ConsentFormData).
     */
    fun showConsentFormDialogFragment(activity: FragmentActivity, consentFormData: ConsentFormData) {
        ConsentDialogFragment.show(activity, consentFormData)
    }

    /**
     * Display consent form on DialogFragment.
     *
     * @param fragment Calling Fragment reference. This Fragment needs to implement ConsentResultsListener.
     * @param consentFormData Data object containing all needed info display the form (@see ConsentFormData).
     */
    fun showConsentFormDialogFragment(fragment: Fragment, consentFormData: ConsentFormData) {
        ConsentDialogFragment.show(fragment, consentFormData)
    }

    /**
     * Display consent form Activity.
     *
     * @param activity Calling Activity reference. This Activity needs to implement onActivityResult to get result.
     * @param consentFormData Data object containing all needed info display the form (@see ConsentFormData).
     * @param requestCode Unique request code used in onActivityResult to determine corresponding result.
     */
    fun startConsentFormActivity(activity: Activity, consentFormData: ConsentFormData, requestCode: Int) =
        ConsentActivity.start(activity, consentFormData, requestCode)

    /**
     * Parse out consentResults HashMap<consentKey, grantResult> from activity result.
     *
     * @param data Intent containing consentResults.
     */
    fun parseOutConsentResults(data: Intent?): HashMap<String, Boolean> {
        ConsentHelper.restoreConsentResults(data?.extras).let {
            return it ?: throw UnknownError()
        }
    }

    /**
     * Create instance of consent form Fragment. To register ConsentResultsListener you need to call
     * registerConsentResultsListener().
     *
     * @param consentFormData Data object containing all needed info display the form (@see ConsentFormData).
     */
    fun createConsenFromFragment(consentFormData: ConsentFormData) = ConsentFragment.newInstance(consentFormData)

}