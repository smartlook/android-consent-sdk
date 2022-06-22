package com.smartlook.consentsdk.ui.consent.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.StyleRes
import com.smartlook.consentsdk.R
import com.smartlook.consentsdk.data.ConsentFormData
import com.smartlook.consentsdk.data.ConsentFormDisplayOptions
import com.smartlook.consentsdk.listeners.ConsentResultsListener
import com.smartlook.consentsdk.ui.consent.ConsentFormBase

class ConsentFormDialog : Dialog {

    private var consentFormData: ConsentFormData
    private var consentResultsListener: ConsentResultsListener
    private var consentFormDisplayOptions: ConsentFormDisplayOptions

    @StyleRes
    private var styleId: Int? = null

    private lateinit var root: View

    constructor(context: Context,
                consentFormData: ConsentFormData,
                consentFormDisplayOptions: ConsentFormDisplayOptions,
                consentResultsListener: ConsentResultsListener) : super(context) {

        this.consentFormData = consentFormData
        this.consentFormDisplayOptions = consentFormDisplayOptions
        this.consentResultsListener = consentResultsListener
    }

    constructor(context: Context,
                consentFormData: ConsentFormData,
                consentFormDisplayOptions: ConsentFormDisplayOptions,
                styleId: Int,
                consentResultsListener: ConsentResultsListener) : super(context, styleId) {

        this.consentFormData = consentFormData
        this.consentFormDisplayOptions = consentFormDisplayOptions
        this.styleId = styleId
        this.consentResultsListener = consentResultsListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val layout = if (consentFormDisplayOptions.consentFormDescriptionScrollingOnly) {
            R.layout.layout_consent_sticky_bottom
        } else {
            R.layout.layout_consent
        }

        setContentView(layout)

        root = findViewById(R.id.root)
    }

    override fun show() {
        setCancelable(false)
        super.show()

        with(window ?: return) {
            setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT)
        }

        ConsentFormBase(consentFormData, root, createResultListener(), styleId = styleId).displayConsent()
    }

    private fun createResultListener(): ConsentFormBase.ResultListener {
        return object : ConsentFormBase.ResultListener {
            override fun onResult(consentResults: HashMap<String, Boolean>) {
                dismiss()
                consentResultsListener.onConsentResults(consentResults)
            }
        }
    }
}
