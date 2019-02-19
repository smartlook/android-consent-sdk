package com.smartlook.consentsdk.ui.consent.dialog

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smartlook.consentsdk.R
import com.smartlook.consentsdk.data.ConsentFormData
import com.smartlook.consentsdk.helpers.ConsentHelper
import com.smartlook.consentsdk.helpers.UtilsHelper
import com.smartlook.consentsdk.listeners.ConsentResultsListener
import com.smartlook.consentsdk.ui.consent.ConsentBase
import kotlinx.android.synthetic.main.consent_dialog.*
import java.security.InvalidParameterException

class ConsentFormDialogFragment : DialogFragment() {

    companion object {

        private const val CALLED_FROM_ACTIVITY = 0
        private const val CALLED_FROM_FRAGMENT = 1

        private const val CONSENT_DIALOG_FRAGMENT_TAG = "consent_dialog_fragment"
        private const val CONSENT_DIALOG_FRAGMENT_CALLER_TYPE = "consent_dialog_fragment_caller_type"

        fun show(activity: FragmentActivity, consentFormData: ConsentFormData, styleId: Int? = null) {
            val consentDialogFragment = ConsentFormDialogFragment().apply {
                arguments = consentFormData.createBundle().apply {
                    putInt(CONSENT_DIALOG_FRAGMENT_CALLER_TYPE, CALLED_FROM_ACTIVITY)
                    putInt(UtilsHelper.STYLE_ID_EXTRA, styleId ?: View.NO_ID)
                }
            }

            consentDialogFragment.show(activity.supportFragmentManager, CONSENT_DIALOG_FRAGMENT_TAG)
        }

        fun show(fragment: Fragment, consentFormData: ConsentFormData, styleId: Int? = null) {
            val consentDialogFragment = ConsentFormDialogFragment().apply {
                arguments = consentFormData.createBundle().apply {
                    putInt(CONSENT_DIALOG_FRAGMENT_CALLER_TYPE, CALLED_FROM_FRAGMENT)
                    putInt(UtilsHelper.STYLE_ID_EXTRA, styleId ?: View.NO_ID)
                }
            }

            consentDialogFragment.show(fragment.childFragmentManager, CONSENT_DIALOG_FRAGMENT_TAG)
        }
    }

    private lateinit var consentBase: ConsentBase
    private lateinit var consentResultsListener: ConsentResultsListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val styleId = UtilsHelper.getStyleId(arguments)
        if (styleId != null) {
            setStyle(DialogFragment.STYLE_NORMAL, styleId)
        }

        isCancelable = false
        registerListener(arguments?.getInt(CONSENT_DIALOG_FRAGMENT_CALLER_TYPE))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.consent_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val consentFormData = ConsentFormData.constructFromBundle(arguments) ?: throw InvalidParameterException()

        consentBase = ConsentBase(
                consentFormData,
                root,
                createResultListener(),
                ConsentHelper.restoreConsentResults(savedInstanceState)
        )

        consentBase.displayConsent()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        ConsentHelper.storeConsentResults(outState, consentBase.consentResults)
    }

    private fun registerListener(callerType: Int?) {
        when (callerType) {
            CALLED_FROM_ACTIVITY -> registerActivityListener()
            CALLED_FROM_FRAGMENT -> registerFragmentListener()
            else -> throw InvalidParameterException("Invalid caller type")
        }
    }

    private fun registerActivityListener() {
        try {
            consentResultsListener = activity as ConsentResultsListener
        } catch (e: ClassCastException) {
            throw ClassCastException("Calling activity must implement ConsentResultsListener interface")
        }
    }

    private fun registerFragmentListener() {
        try {
            consentResultsListener = parentFragment as ConsentResultsListener
        } catch (e: ClassCastException) {
            throw ClassCastException("Calling fragment must implement ConsentResultsListener interface")
        }
    }

    private fun createResultListener(): ConsentBase.ResultListener {
        return object : ConsentBase.ResultListener {
            override fun onResult(consentResults: HashMap<String, Boolean>) {
                consentResultsListener.onConsentResults(consentResults)
                dismiss()
            }
        }
    }
}
