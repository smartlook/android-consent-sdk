package com.smartlook.consentsdk.ui.consent.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.annotation.StyleRes
import com.smartlook.consentsdk.R
import com.smartlook.consentsdk.data.ConsentFormData
import com.smartlook.consentsdk.listeners.ConsentResultsListener
import com.smartlook.consentsdk.ui.consent.ConsentFormBase
import kotlinx.android.synthetic.main.consent_dialog.*

class ConsentFormDialog : Dialog {

    private var consentFormData: ConsentFormData
    private var consentResultsListener: ConsentResultsListener
    @StyleRes
    private var styleId: Int? = null

    constructor(context: Context,
                consentFormData: ConsentFormData,
                consentResultsListener: ConsentResultsListener) : super(context) {

        this.consentFormData = consentFormData
        this.consentResultsListener = consentResultsListener
    }

    constructor(context: Context,
                consentFormData: ConsentFormData,
                styleId: Int,
                consentResultsListener: ConsentResultsListener) : super(context, styleId) {

        this.consentFormData = consentFormData
        this.consentResultsListener = consentResultsListener
        this.styleId = styleId
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.consent_dialog)
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
