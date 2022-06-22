package com.smartlook.consentsdk

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.smartlook.consentsdk.data.ConsentFormData
import com.smartlook.consentsdk.data.ConsentFormDisplayOptions
import com.smartlook.consentsdk.helpers.ConsentHelper
import com.smartlook.consentsdk.helpers.SharedPreferencesHelper
import com.smartlook.consentsdk.listeners.ConsentResultsListener
import com.smartlook.consentsdk.ui.consent.activity.ConsentFormActivity
import com.smartlook.consentsdk.ui.consent.dialog.ConsentFormDialog
import com.smartlook.consentsdk.ui.consent.dialog.ConsentFormDialogFragment
import com.smartlook.consentsdk.ui.consent.fragment.ConsentFormFragment

class ConsentSDK(applicationContext: Context) : ContextWrapper(applicationContext) {

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
    fun saveConsentResult(consentKey: String,
                          grantResult: Boolean) =
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
     * @param consentFormDisplayOptions (Optional) Data object containing options for displaying the consent form (@see ConsentFormDisplayOptions).
     * @param consentResultsListener Callback called on successful fill of consent form (@see ConsentResultsListener).
     */
    fun showConsentFormDialog(activity: Activity,
                              consentFormData: ConsentFormData,
                              consentFormDisplayOptions: ConsentFormDisplayOptions = ConsentFormDisplayOptions(),
                              consentResultsListener: ConsentResultsListener) =
            ConsentFormDialog(activity, consentFormData, consentFormDisplayOptions, consentResultsListener).show()

    /**
     * Display consent form on Dialog. If you want to correctly persist on orientation change use
     * showConsentFormDialogFragment().
     *
     * @param activity Calling Activity reference.
     * @param consentFormData Data object containing all needed info display the form (@see ConsentFormData).
     * @param consentFormDisplayOptions (Optional) Data object containing options for displaying the consent form (@see ConsentFormDisplayOptions).
     * @param consentResultsListener Callback called on successful fill of consent form (@see ConsentResultsListener).
     * @param styleId Reference to style resource. Can be used to style Dialog.
     */
    fun showConsentFormDialog(activity: Activity,
                              consentFormData: ConsentFormData,
                              consentFormDisplayOptions: ConsentFormDisplayOptions = ConsentFormDisplayOptions(),
                              @StyleRes styleId: Int,
                              consentResultsListener: ConsentResultsListener) =
            ConsentFormDialog(activity, consentFormData, consentFormDisplayOptions, styleId, consentResultsListener).show()

    /**
     * Display consent form on DialogFragment.
     *
     * @param activity Calling Activity reference. This Activity needs to implement ConsentResultsListener.
     * @param consentFormData Data object containing all needed info display the form (@see ConsentFormData).
     * @param consentFormDisplayOptions (Optional) Data object containing options for displaying the consent form (@see ConsentFormDisplayOptions).
     */
    fun showConsentFormDialogFragment(activity: FragmentActivity,
                                      consentFormData: ConsentFormData,
                                      consentFormDisplayOptions: ConsentFormDisplayOptions = ConsentFormDisplayOptions()) {
        ConsentFormDialogFragment.show(activity, consentFormData, consentFormDisplayOptions)
    }

    /**
     * Display consent form on DialogFragment.
     *
     * @param activity Calling Activity reference. This Activity needs to implement ConsentResultsListener.
     * @param consentFormData Data object containing all needed info display the form (@see ConsentFormData).
     * @param consentFormDisplayOptions (Optional) Data object containing options for displaying the consent form (@see ConsentFormDisplayOptions).
     * @param styleId Reference to style resource. Can be used to style DialogFragment.
     */
    fun showConsentFormDialogFragment(activity: FragmentActivity,
                                      consentFormData: ConsentFormData,
                                      consentFormDisplayOptions: ConsentFormDisplayOptions = ConsentFormDisplayOptions(),
                                      @StyleRes styleId: Int) {
        ConsentFormDialogFragment.show(activity, consentFormData, consentFormDisplayOptions, styleId)
    }

    /**
     * Display consent form on DialogFragment.
     *
     * @param fragment Calling Fragment reference. This Fragment needs to implement ConsentResultsListener.
     * @param consentFormData Data object containing all needed info display the form (@see ConsentFormData).
     * @param consentFormDisplayOptions (Optional) Data object containing options for displaying the consent form (@see ConsentFormDisplayOptions).
     */
    fun showConsentFormDialogFragment(fragment: Fragment,
                                      consentFormData: ConsentFormData,
                                      consentFormDisplayOptions: ConsentFormDisplayOptions = ConsentFormDisplayOptions(),
    ) {
        ConsentFormDialogFragment.show(fragment, consentFormData,consentFormDisplayOptions)
    }

    /**
     * Display consent form on DialogFragment.
     *
     * @param fragment Calling Fragment reference. This Fragment needs to implement ConsentResultsListener.
     * @param consentFormData Data object containing all needed info display the form (@see ConsentFormData).
     * @param consentFormDisplayOptions (Optional) Data object containing options for displaying the consent form (@see ConsentFormDisplayOptions).
     * @param styleId Reference to style resource. Can be used to style DialogFragment.
     */
    fun showConsentFormDialogFragment(fragment: Fragment,
                                      consentFormData: ConsentFormData,
                                      consentFormDisplayOptions: ConsentFormDisplayOptions = ConsentFormDisplayOptions(),
                                      @StyleRes styleId: Int) {
        ConsentFormDialogFragment.show(fragment, consentFormData, consentFormDisplayOptions, styleId)
    }

    /**
     * Display consent form Activity.
     *
     * @param activity Calling Activity reference. This Activity needs to implement onActivityResult to get result.
     * @param consentFormData Data object containing all needed info display the form (@see ConsentFormData).
     * @param consentFormDisplayOptions (Optional) Data object containing options for displaying the consent form (@see ConsentFormDisplayOptions).
     * @param requestCode Unique request code used in onActivityResult to determine corresponding result.
     */
    fun startConsentFormActivity(activity: Activity,
                                 consentFormData: ConsentFormData,
                                 consentFormDisplayOptions: ConsentFormDisplayOptions = ConsentFormDisplayOptions(),
                                 requestCode: Int) =
            ConsentFormActivity.start(activity, consentFormData, consentFormDisplayOptions, requestCode)

    /**
     * Display consent form Activity.
     *
     * @param activity Calling Activity reference. This Activity needs to implement onActivityResult to get result.
     * @param consentFormData Data object containing all needed info display the form (@see ConsentFormData).
     * @param consentFormDisplayOptions (Optional) Data object containing options for displaying the consent form (@see ConsentFormDisplayOptions).
     * @param requestCode Unique request code used in onActivityResult to determine corresponding result.
     * @param styleId Reference to style resource. Can be used to style Activity.
     */
    fun startConsentFormActivity(activity: Activity,
                                 consentFormData: ConsentFormData,
                                 consentFormDisplayOptions: ConsentFormDisplayOptions = ConsentFormDisplayOptions(),
                                 requestCode: Int,
                                 @StyleRes styleId: Int) =
            ConsentFormActivity.start(activity, consentFormData, consentFormDisplayOptions, requestCode, styleId)

    /**
     * Parse out consentResults HashMap<consentKey, grantResult> from activity result.
     *
     * @param data Intent containing consentResults.
     * @param consentFormDisplayOptions (Optional) Data object containing options for displaying the consent form (@see ConsentFormDisplayOptions).
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
     * @param consentFormDisplayOptions (Optional) Data object containing options for displaying the consent form (@see ConsentFormDisplayOptions).
     */
    fun createConsentFormFragment(
        consentFormData: ConsentFormData,
        consentFormDisplayOptions: ConsentFormDisplayOptions = ConsentFormDisplayOptions(),
    ) =
        ConsentFormFragment.newInstance(consentFormData, consentFormDisplayOptions)

    /**
     * Create instance of consent form Fragment. To register ConsentResultsListener you need to call
     * registerConsentResultsListener().
     *
     * @param consentFormData Data object containing all needed info display the form (@see ConsentFormData).
     * @param consentFormDisplayOptions (Optional) Data object containing options for displaying the consent form (@see ConsentFormDisplayOptions).
     * @param styleId Reference to style resource. Can be used to style Fragment.
     */
    fun createConsentFormFragment(
        consentFormData: ConsentFormData,
        consentFormDisplayOptions: ConsentFormDisplayOptions = ConsentFormDisplayOptions(),
        @StyleRes styleId: Int
    ) = ConsentFormFragment.newInstance(consentFormData, consentFormDisplayOptions, styleId)

}