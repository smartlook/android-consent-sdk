package com.smartlook.consentsdksample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.smartlook.consentsdk.ConsentSDK
import com.smartlook.consentsdk.data.ConsentFormData
import com.smartlook.consentsdk.data.ConsentFormItem
import com.smartlook.consentsdk.listeners.ConsentResultsListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ConsentResultsListener {

    private lateinit var consentSDK: ConsentSDK

    companion object {
        const val CONSENT_1_KEY = "consent_1_key"
        const val CONSENT_2_KEY = "consent_2_key"

        const val CONSENT_REQUEST_CODE = 10001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        consentSDK = ConsentSDK(this)
        val consentFormData = prepareConsentFormData()

        show_dialog.setOnClickListener {
            consentSDK.showConsentFormDialog(consentFormData, object : ConsentResultsListener {
                override fun onConsentResults(consentResults: HashMap<String, Boolean>) {
                    displayConsentResults(consentResults)
                }
            })
        }

        show_dialog_fragment.setOnClickListener {
            consentSDK.showConsentFormDialogFragment(this, consentFormData)
        }

        start_activity.setOnClickListener {
            consentSDK.startConsentFormActivity(this, consentFormData, CONSENT_REQUEST_CODE)
        }
    }

    override fun onConsentResults(consentResults: HashMap<String, Boolean>) {
        displayConsentResults(consentResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CONSENT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                displayConsentResults(consentSDK.parseOutConsentResults(data))
            }
        }
    }

    private fun prepareConsentFormData(): ConsentFormData {
        return ConsentFormData(
            titleText = getString(R.string.consent_form_title),
            descriptionText = getString(R.string.consent_form_description),
            confirmButtonText = getString(R.string.consent_form_confirm_button_text),
            consentFormItems = prepareConsentFormItems()
        )
    }

    private fun prepareConsentFormItems(): Array<ConsentFormItem> {
        return arrayOf(
            ConsentFormItem(
                consentKey = CONSENT_1_KEY,
                required = true,
                description = getString(R.string.consent_1_description),
                link = null
            ),
            ConsentFormItem(
                consentKey = CONSENT_2_KEY,
                required = false,
                description = getString(R.string.consent_2_description),
                link = getString(R.string.consent_2_link)
            )
        )
    }

    private fun displayConsentResults(consentResults: HashMap<String, Boolean>) {
        //todo display consent results in some sort of list
    }
}
