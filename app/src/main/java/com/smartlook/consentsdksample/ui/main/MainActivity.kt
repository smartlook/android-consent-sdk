package com.smartlook.consentsdksample.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.smartlook.consentsdk.data.ConsentFormData
import com.smartlook.consentsdk.data.ConsentFormItem
import com.smartlook.consentsdk.data.toConsentText
import com.smartlook.consentsdk.listeners.ConsentResultsListener
import com.smartlook.consentsdk.ui.consent.fragment.ConsentFormFragment
import com.smartlook.consentsdksample.App
import com.smartlook.consentsdksample.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ConsentResultsListener {

    companion object {
        const val CONSENT_1_KEY = "consent_1_key"
        const val CONSENT_2_KEY = "consent_2_key"

        const val CONSENT_REQUEST_CODE = 10001

        const val CONSENT_FORM_FRAGMENT_TAG = "consent_form_fragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        format_text.setOnCheckedChangeListener { _, isChecked ->
            setConsentFormData(isChecked)
        }

        setConsentFormData(format_text.isChecked)
    }

    private fun setConsentFormData(formatText: Boolean) {
        prepareConsentFormData(formatText).let {
            displayConsentResults(loadConsentResults(it.consentFormItems))
            handleShowDialog(it)
            handleShowFragmentDialog(it)
            handleStartConsentActivity(it)
            handleShowFragment(it)
        }
    }

    private fun loadConsentResults(consentFormItems: Array<ConsentFormItem>): HashMap<String, Boolean?> {
        return hashMapOf<String, Boolean?>().apply {
            consentFormItems.forEach {
                put(it.consentKey, App.consentSDK.loadConsentResult(it.consentKey))
            }
        }
    }

    // Getting result from DialogFragment
    override fun onConsentResults(consentResults: HashMap<String, Boolean>) {
        displayConsentResults(consentResults as HashMap<String, Boolean?>)
    }

    // Getting result from activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CONSENT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                displayConsentResults(App.consentSDK.parseOutConsentResults(data) as HashMap<String, Boolean?>)
            } else {
                // consent form not filled successfully
            }
        }
    }

    private fun prepareConsentFormData(formatText: Boolean): ConsentFormData {
        return ConsentFormData(
            titleText = getString(R.string.consent_form_title).toConsentText(),
            descriptionText = if (formatText) {
                getString(R.string.consent_form_description_formatted).fromHtml().toConsentText()
            } else {
                getString(R.string.consent_form_description).toConsentText()
            },
            confirmButtonText = getString(R.string.consent_form_confirm_button_text).toConsentText(),
            consentFormItems = prepareConsentFormItems(formatText)
        )
    }

    private fun prepareConsentFormItems(formatText: Boolean): Array<ConsentFormItem> {
        return arrayOf(
            ConsentFormItem(
                consentKey = CONSENT_1_KEY,
                required = true,
                description = if (formatText) {
                    getString(R.string.consent_1_description_formatted).fromHtml().toConsentText()
                } else {
                    getString(R.string.consent_1_description).toConsentText()
                },
                link = null
            ),
            ConsentFormItem(
                consentKey = CONSENT_2_KEY,
                required = false,
                description = if (formatText) {
                    getString(R.string.consent_2_description_formatted).fromHtml().toConsentText()
                } else {
                    getString(R.string.consent_2_description).toConsentText()
                },
                link = getString(R.string.consent_2_link)
            )
        )
    }

    private fun handleStartConsentActivity(consentFormData: ConsentFormData) {
        start_activity.setOnClickListener {
            App.consentSDK.startConsentFormActivity(
                this,
                consentFormData,
                CONSENT_REQUEST_CODE,
                R.style.ActivityStyle
            )
        }
    }

    private fun handleShowFragmentDialog(consentFormData: ConsentFormData) {
        show_dialog_fragment.setOnClickListener {
            App.consentSDK.showConsentFormDialogFragment(this, consentFormData, R.style.DialogStyle)
        }
    }

    private fun handleShowDialog(consentFormData: ConsentFormData) {
        show_dialog.setOnClickListener {
            App.consentSDK.showConsentFormDialog(
                this,
                consentFormData,
                object : ConsentResultsListener {
                    override fun onConsentResults(consentResults: HashMap<String, Boolean>) {
                        displayConsentResults(consentResults as HashMap<String, Boolean?>)
                    }
                })
        }
    }

    private fun handleShowFragment(consentFormData: ConsentFormData) {
        show_fragment.setOnClickListener {
            with(supportFragmentManager) {
                beginTransaction()
                    .replace(
                        R.id.fragment_placeholder,
                        App.consentSDK.createConsentFormFragment(
                            consentFormData,
                            R.style.ActivityStyle
                        ),
                        CONSENT_FORM_FRAGMENT_TAG
                    )
                    .commit()
                executePendingTransactions()
            }

            show_fragment.isEnabled = false

            with(supportFragmentManager.findFragmentByTag(CONSENT_FORM_FRAGMENT_TAG) as ConsentFormFragment) {
                registerConsentResultsListener(createConsentFormFragmentResultsListener())
            }
        }

    }

    private fun createConsentFormFragmentResultsListener(): ConsentResultsListener {
        return object : ConsentResultsListener {
            override fun onConsentResults(consentResults: HashMap<String, Boolean>) {
                displayConsentResults(consentResults as HashMap<String, Boolean?>)

                with(supportFragmentManager.beginTransaction()) {
                    remove(supportFragmentManager.findFragmentByTag(CONSENT_FORM_FRAGMENT_TAG) as ConsentFormFragment)
                    commit()
                }

                show_fragment.isEnabled = true
            }
        }
    }

    private fun displayConsentResults(consentResults: HashMap<String, Boolean?>) {
        with(consent_results) {
            hasFixedSize()
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ConsentResultsAdapter(context!!, consentResults)
        }
    }
}
