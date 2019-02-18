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
        const val CONSENT_RESULTS_EXTRA = "CONSENT_RESULTS_EXTRA"

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
        consentBase = ConsentBase(consentFormData, root, createResultListener(), restoreConsentResults(savedInstanceState))
        consentBase.displayConsent()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(CONSENT_RESULTS_EXTRA, consentBase.consentResults)
    }

    private fun restoreConsentResults(savedInstanceState: Bundle?): HashMap<String, Boolean>? {
        return if (savedInstanceState == null || !savedInstanceState.containsKey(CONSENT_RESULTS_EXTRA)) {
            null
        } else {
            savedInstanceState.getSerializable(CONSENT_RESULTS_EXTRA) as HashMap<String, Boolean>
        }
    }

    private fun createResultListener(): ConsentBase.ResultListener {
        return object : ConsentBase.ResultListener {
            override fun onResult(consentResults: HashMap<String, Boolean>) {
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(CONSENT_RESULTS_EXTRA, consentResults)
                })
                finish()
            }
        }
    }
}