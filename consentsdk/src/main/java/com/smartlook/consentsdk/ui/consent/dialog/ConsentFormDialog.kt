package com.smartlook.consentsdk.ui.consent.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.smartlook.consentsdk.R
import com.smartlook.consentsdk.data.ConsentFormData
import com.smartlook.consentsdk.listeners.ConsentResultsListener
import com.smartlook.consentsdk.ui.consent.ConsentBase
import kotlinx.android.synthetic.main.consent_dialog.*

class ConsentFormDialog(context: Context,
                        private val consentFormData: ConsentFormData,
                        private val consentResultsListener: ConsentResultsListener) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.consent_dialog)

        ConsentBase(consentFormData, root, createResultListener()).displayConsent()
    }

    override fun show() {
        setCancelable(false)
        super.show()

        with(window ?: return) {
            setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT)
        }
    }

    private fun createResultListener(): ConsentBase.ResultListener {
        return object : ConsentBase.ResultListener {
            override fun onResult(consentResults: HashMap<String, Boolean>) {
                dismiss()
                consentResultsListener.onConsentResults(consentResults)
            }
        }
    }
}
