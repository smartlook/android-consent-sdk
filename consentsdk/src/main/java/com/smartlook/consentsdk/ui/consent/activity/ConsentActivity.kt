package com.smartlook.consentsdk.ui.consent.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.smartlook.consentsdk.R
import com.smartlook.consentsdk.data.ConsentFormData
import com.smartlook.consentsdk.ui.consent.ConsentBase
import kotlinx.android.synthetic.main.consent_dialog.*
import java.security.InvalidParameterException

class ConsentActivity : AppCompatActivity() {

    companion object {
        const val CONSENT_KEYS_EXTRA = "CONSENT_KEYS_EXTRA"
        const val GRANT_RESULTS_EXTRA = "GRANT_RESULTS_EXTRA"

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

        consentFormData = ConsentFormData.constructFromBundle(intent.extras) ?: throw InvalidParameterException()
        consentBase = ConsentBase(consentFormData, root, createResultListener(), restoreGrantResults(savedInstanceState))
        consentBase.displayConsent()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBooleanArray(GRANT_RESULTS_EXTRA, consentBase.grantResults)
    }

    private fun restoreGrantResults(savedInstanceState: Bundle?): BooleanArray? {
        return if (savedInstanceState == null || !savedInstanceState.containsKey(GRANT_RESULTS_EXTRA)) {
            null
        } else {
            savedInstanceState.getBooleanArray(GRANT_RESULTS_EXTRA)
        }
    }

    private fun createResultListener(): ConsentBase.ResultListener {
        return object : ConsentBase.ResultListener {
            override fun onResult(consentKeys: Array<String>, grantResults: BooleanArray) {
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(CONSENT_KEYS_EXTRA, consentKeys)
                    putExtra(GRANT_RESULTS_EXTRA, consentKeys)
                })
                finish()
            }
        }
    }
}