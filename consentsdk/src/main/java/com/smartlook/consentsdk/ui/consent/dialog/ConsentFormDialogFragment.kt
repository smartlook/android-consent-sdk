package com.smartlook.consentsdk.ui.consent.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.smartlook.consentsdk.R
import com.smartlook.consentsdk.data.ConsentFormData
import com.smartlook.consentsdk.data.ConsentFormDisplayOptions
import com.smartlook.consentsdk.helpers.ConsentHelper
import com.smartlook.consentsdk.helpers.UtilsHelper
import com.smartlook.consentsdk.listeners.ConsentResultsListener
import com.smartlook.consentsdk.ui.consent.ConsentFormBase
import java.security.InvalidParameterException

class ConsentFormDialogFragment : DialogFragment() {

    companion object {

        private const val CALLED_FROM_ACTIVITY = 0
        private const val CALLED_FROM_FRAGMENT = 1

        private const val CONSENT_DIALOG_FRAGMENT_TAG = "consent_dialog_fragment"
        private const val CONSENT_DIALOG_FRAGMENT_CALLER_TYPE =
            "consent_dialog_fragment_caller_type"

        fun show(
            activity: FragmentActivity,
            consentFormData: ConsentFormData,
            consentFormDisplayOptions: ConsentFormDisplayOptions,
            @StyleRes styleId: Int? = null
        ) {
            val consentDialogFragment = ConsentFormDialogFragment().apply {
                val bundle = consentFormData.createBundle().apply {
                    putInt(CONSENT_DIALOG_FRAGMENT_CALLER_TYPE, CALLED_FROM_ACTIVITY)
                    putInt(UtilsHelper.STYLE_ID_EXTRA, styleId ?: View.NO_ID)
                }
                arguments = consentFormDisplayOptions.saveToBundle(bundle)
            }

            consentDialogFragment.show(activity.supportFragmentManager, CONSENT_DIALOG_FRAGMENT_TAG)
        }

        fun show(
            fragment: Fragment,
            consentFormData: ConsentFormData,
            consentFormDisplayOptions: ConsentFormDisplayOptions,
            @StyleRes styleId: Int? = null
        ) {
            val consentDialogFragment = ConsentFormDialogFragment().apply {
                val bundle = consentFormData.createBundle().apply {
                    putInt(CONSENT_DIALOG_FRAGMENT_CALLER_TYPE, CALLED_FROM_ACTIVITY)
                    putInt(UtilsHelper.STYLE_ID_EXTRA, styleId ?: View.NO_ID)
                }
                arguments = consentFormDisplayOptions.saveToBundle(bundle)
            }

            consentDialogFragment.show(fragment.childFragmentManager, CONSENT_DIALOG_FRAGMENT_TAG)
        }
    }

    private lateinit var consentFormBase: ConsentFormBase
    private lateinit var consentResultsListener: ConsentResultsListener
    private lateinit var consentFormDisplayOptions: ConsentFormDisplayOptions


    @StyleRes
    private var styleId: Int? = null

    private lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        styleId = handleStyle()
        isCancelable = false
        registerListener(arguments?.getInt(CONSENT_DIALOG_FRAGMENT_CALLER_TYPE))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        consentFormDisplayOptions = ConsentFormDisplayOptions.constructFromBundle(arguments)
            ?: throw InvalidParameterException()

        val layout = if (consentFormDisplayOptions.consentFormDescriptionScrollingOnly) {
            R.layout.layout_consent_sticky_bottom
        } else {
            R.layout.layout_consent
        }
        return inflater.inflate(layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        root = view.findViewById(R.id.root)

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

    @StyleRes
    private fun handleStyle(): Int? {
        return UtilsHelper.getStyleId(arguments)?.also {
            setStyle(STYLE_NORMAL, it)
        }
    }

    private fun createResultListener(): ConsentFormBase.ResultListener {
        return object : ConsentFormBase.ResultListener {
            override fun onResult(consentResults: HashMap<String, Boolean>) {
                consentResultsListener.onConsentResults(consentResults)
                dismiss()
            }
        }
    }
}
