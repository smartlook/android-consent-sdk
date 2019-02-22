package com.smartlook.consentsdk.ui.consent.fragment

import android.os.Bundle
import android.support.annotation.StyleRes
import android.support.v4.app.Fragment
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smartlook.consentsdk.R
import com.smartlook.consentsdk.data.ConsentFormData
import com.smartlook.consentsdk.helpers.ConsentHelper
import com.smartlook.consentsdk.helpers.UtilsHelper
import com.smartlook.consentsdk.listeners.ConsentResultsListener
import com.smartlook.consentsdk.ui.consent.ConsentFormBase
import kotlinx.android.synthetic.main.consent_dialog.*
import java.security.InvalidParameterException

class ConsentFormFragment : Fragment() {

    companion object {
        fun newInstance(consentFormData: ConsentFormData, @StyleRes styleId: Int? = null): ConsentFormFragment {
            return ConsentFormFragment().apply {
                arguments = consentFormData.createBundle().apply {
                    putInt(UtilsHelper.STYLE_ID_EXTRA, styleId ?: View.NO_ID)
                }
            }
        }
    }

    private lateinit var consentFormBase: ConsentFormBase
    private var consentResultsListener: ConsentResultsListener? = null
    @StyleRes private var styleId: Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        var localInflater = inflater

        UtilsHelper.getStyleId(arguments)?.also {
            styleId = it
            localInflater = inflater.cloneInContext(ContextThemeWrapper(activity, it))
        }

        return localInflater.inflate(R.layout.consent_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val consentFormData = ConsentFormData.constructFromBundle(arguments)
                ?: throw InvalidParameterException()

        consentFormBase = ConsentFormBase(
                consentFormData,
                root,
                createResultListener(),
                ConsentHelper.restoreConsentResults(savedInstanceState),
                styleId
        )

        consentFormBase.displayConsent()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        ConsentHelper.storeConsentResults(outState, consentFormBase.consentResults)
    }

    fun registerConsentResultsListener(listener: ConsentResultsListener) {
        consentResultsListener = listener
    }

    private fun createResultListener(): ConsentFormBase.ResultListener {
        return object : ConsentFormBase.ResultListener {
            override fun onResult(consentResults: HashMap<String, Boolean>) {
                consentResultsListener?.onConsentResults(consentResults)
            }
        }
    }

}