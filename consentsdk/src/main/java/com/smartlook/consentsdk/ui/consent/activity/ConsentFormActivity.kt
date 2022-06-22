package com.smartlook.consentsdk.ui.consent.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import com.smartlook.consentsdk.R
import com.smartlook.consentsdk.data.ConsentFormData
import com.smartlook.consentsdk.helpers.ConsentHelper
import com.smartlook.consentsdk.helpers.UtilsHelper
import com.smartlook.consentsdk.ui.consent.ConsentFormBase
import kotlinx.android.synthetic.main.consent_dialog.*
import java.security.InvalidParameterException

class ConsentFormActivity : AppCompatActivity() {

    companion object {

        fun start(activity: Activity,
                  consentFormData: ConsentFormData,
                  requestCode: Int,
                  @StyleRes styleId: Int? = null) {

            val intent = Intent(activity, ConsentFormActivity::class.java).apply {
                putExtras(consentFormData.createBundle().apply {
                    putInt(UtilsHelper.STYLE_ID_EXTRA, styleId ?: View.NO_ID)
                })
            }

            activity.startActivityForResult(intent, requestCode)
        }
    }

    private lateinit var consentFormData: ConsentFormData
    private lateinit var consentFormBase: ConsentFormBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @StyleRes val styleId = handleStyle()
        setContentView(R.layout.consent_activity)
        hideToolbar()

        consentFormData = ConsentFormData.constructFromBundle(intent.extras)
                ?: throw InvalidParameterException()

        consentFormBase = ConsentFormBase(
                consentFormData,
                root,
                createResultListener(),
                ConsentHelper.restoreConsentResults(savedInstanceState),
                styleId)

        consentFormBase.displayConsent()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        ConsentHelper.storeConsentResults(outState, consentFormBase.consentResults)
    }

    private fun hideToolbar() {
        actionBar?.hide()
        supportActionBar?.hide()
    }

    @StyleRes
    private fun handleStyle(): Int? {
        return UtilsHelper.getStyleId(intent.extras)?.also {
            setTheme(it)
        }
    }

    private fun createResultListener(): ConsentFormBase.ResultListener {
        return object : ConsentFormBase.ResultListener {
            override fun onResult(consentResults: HashMap<String, Boolean>) {
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(ConsentHelper.CONSENT_RESULTS_EXTRA, consentResults)
                })
                finish()
            }
        }
    }

}