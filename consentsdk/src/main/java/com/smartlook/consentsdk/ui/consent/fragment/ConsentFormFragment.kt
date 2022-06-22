package com.smartlook.consentsdk.ui.consent.fragment

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import com.smartlook.consentsdk.R
import com.smartlook.consentsdk.data.ConsentFormData
import com.smartlook.consentsdk.data.ConsentFormDisplayOptions
import com.smartlook.consentsdk.helpers.ConsentHelper
import com.smartlook.consentsdk.helpers.UtilsHelper
import com.smartlook.consentsdk.listeners.ConsentResultsListener
import com.smartlook.consentsdk.ui.consent.ConsentFormBase
import java.security.InvalidParameterException

class ConsentFormFragment : Fragment() {

    companion object {
        fun newInstance(
            consentFormData: ConsentFormData,
            consentFormDisplayOptions: ConsentFormDisplayOptions,
            @StyleRes styleId: Int? = null
        ): ConsentFormFragment {
            return ConsentFormFragment().apply {
                val bundle = consentFormData.createBundle().apply {
                    putInt(UtilsHelper.STYLE_ID_EXTRA, styleId ?: View.NO_ID)
                }
                arguments = consentFormDisplayOptions.saveToBundle(bundle)
            }
        }
    }

    private lateinit var consentFormBase: ConsentFormBase
    private lateinit var consentFormDisplayOptions: ConsentFormDisplayOptions
    private var consentResultsListener: ConsentResultsListener? = null
    @StyleRes
    private var styleId: Int? = null

    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        var localInflater = inflater

        UtilsHelper.getStyleId(arguments)?.also {
            styleId = it
            localInflater = inflater.cloneInContext(ContextThemeWrapper(activity, it))
        }

        consentFormDisplayOptions = ConsentFormDisplayOptions.constructFromBundle(arguments)
            ?: throw InvalidParameterException()

        val layout = if (consentFormDisplayOptions.consentFormDescriptionScrollingOnly) {
            R.layout.layout_consent_sticky_bottom
        } else {
            R.layout.layout_consent
        }

        return localInflater.inflate(layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val consentFormData = ConsentFormData.constructFromBundle(arguments)
            ?: throw InvalidParameterException()

        root = view.findViewById(R.id.root)

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