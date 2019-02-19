package com.smartlook.consentsdk.ui.consent.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smartlook.consentsdk.R
import com.smartlook.consentsdk.data.ConsentFormData
import com.smartlook.consentsdk.helpers.ConsentHelper
import com.smartlook.consentsdk.listeners.ConsentResultsListener
import com.smartlook.consentsdk.ui.consent.ConsentBase
import kotlinx.android.synthetic.main.consent_dialog.*
import java.security.InvalidParameterException

class ConsentFormFragment : Fragment() {

    companion object {
        fun newInstance(consentFormData: ConsentFormData): ConsentFormFragment {
            return ConsentFormFragment().apply {
                arguments = consentFormData.createBundle()
            }
        }
    }

    private lateinit var consentBase: ConsentBase
    private var consentResultsListener: ConsentResultsListener? = null

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

    fun registerConsentResultsListener(listener: ConsentResultsListener) {
        consentResultsListener = listener
    }

    private fun createResultListener(): ConsentBase.ResultListener {
        return object : ConsentBase.ResultListener {
            override fun onResult(consentResults: HashMap<String, Boolean>) {
                consentResultsListener?.onConsentResults(consentResults)
            }
        }
    }

}