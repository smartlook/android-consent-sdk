package com.smartlook.consentsdk.ui.consent.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.smartlook.consentsdk.R
import com.smartlook.consentsdk.data.ConsentFormData
import com.smartlook.consentsdk.helpers.ConsentHelper
import com.smartlook.consentsdk.ui.consent.ConsentBase
import kotlinx.android.synthetic.main.consent_dialog.*
import java.security.InvalidParameterException

class ConsentActivity : AppCompatActivity() {

    companion object {
        fun start(activity: Activity, consentFormData: ConsentFormData, requestCode: Int) {
            activity.startActivityForResult(
                    Intent(activity, ConsentActivity::class.java).apply { putExtras(consentFormData.createBundle()) },
                    requestCode)
        }
    }

    private lateinit var consentFormData: ConsentFormData
    private lateinit var consentBase: ConsentBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.consent_activity)
        hideToolbar()

        consentFormData = ConsentFormData.constructFromBundle(intent.extras) ?: throw InvalidParameterException()

        consentBase = ConsentBase(
            consentFormData,
            root,
            createResultListener(),
            ConsentHelper.restoreConsentResults(savedInstanceState))

        consentBase.displayConsent()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        ConsentHelper.storeConsentResults(outState, consentBase.consentResults)
    }

    private fun hideToolbar() {
        actionBar?.hide()
        supportActionBar?.hide()
    }

    private fun createResultListener(): ConsentBase.ResultListener {
        return object : ConsentBase.ResultListener {
            override fun onResult(consentResults: HashMap<String, Boolean>) {
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(ConsentHelper.CONSENT_RESULTS_EXTRA, consentResults)
                })
                finish()
            }
        }
    }

}