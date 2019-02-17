package com.smartlook.consentsdk.ui.consent

import android.content.ContextWrapper
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.smartlook.consentsdk.ConsentSDK
import com.smartlook.consentsdk.R
import com.smartlook.consentsdk.data.ConsentFormData
import com.smartlook.consentsdk.data.ConsentFormItem
import com.smartlook.consentsdk.helpers.UtilsHelper
import com.smartlook.consentsdk.listeners.ConsentItemListener

class ConsentBase(
    private val consentFormData: ConsentFormData,
    rootView: View,
    private val resultListener: ResultListener,
    grantResults: BooleanArray? = null) : ContextWrapper(rootView.context) {

    private val consentApi = ConsentSDK(this)

    var consentKeys: Array<String>
    var grantResults: BooleanArray

    // We are doing it oldschool because it works for both dialog and activity
    private val tvTitle = rootView.findViewById<TextView>(R.id.consent_title)
    private val tvDescription = rootView.findViewById<TextView>(R.id.consent_description)
    private val lvConsentItemsRoot = rootView.findViewById<LinearLayout>(R.id.consent_items_root)
    private val bConfirm = rootView.findViewById<Button>(R.id.consent_confirm_button)

    init {
        this.consentKeys = obtainConsentKeys(consentFormData.consentFormItems)
        this.grantResults = grantResults ?: obtainGrantResults(consentFormData.consentFormItems)
    }

    fun displayConsent() {
        displayTexts()
        displayConsentItems()

        updateConfirmButton()
        handleConfirmButton()
    }

    private fun updateConfirmButton() {
        var enable = true

        consentFormData.consentFormItems.forEachIndexed { index, item ->
            if (item.required && !grantResults[index]) {
                enable = false
            }
        }

        bConfirm.isEnabled = enable
    }

    private fun displayTexts() {
        with(consentFormData) {
            tvTitle.text = UtilsHelper.stringFromResourceOrString(this@ConsentBase, titleText)
            tvDescription.text = UtilsHelper.stringFromResourceOrString(this@ConsentBase, descriptionText)
            bConfirm.text = UtilsHelper.stringFromResourceOrString(this@ConsentBase, confirmButtonText)
        }
    }

    // recycler view should have nested scroll
    private fun displayConsentItems() {
        consentFormData.consentFormItems.forEachIndexed { index, consentItem ->
            lvConsentItemsRoot.addView(ConsentItemView(this).apply {
                setData(grantResults[index], consentItem)
                registerListener(index, createConsentItemListener())
            })
        }
    }

    private fun handleConfirmButton() {
        bConfirm.setOnClickListener {
            storeGrantResults()
            resultListener.onResult(consentKeys, grantResults)
        }
    }

    private fun storeGrantResults() {
        consentApi.setConsentResultStored()

        consentKeys.forEachIndexed { index, key ->
            consentApi.saveConsentResult(key, grantResults[index])
        }
    }

    private fun obtainGrantResults(consentFormItems: Array<ConsentFormItem>) =
        consentFormItems.map {
            consentApi.loadConsentResult(UtilsHelper.stringFromResourceOrString(this, it.key))
        }.toBooleanArray()

    private fun obtainConsentKeys(consentFormItems: Array<ConsentFormItem>) =
        consentFormItems.map {
            UtilsHelper.stringFromResourceOrString(this, it.key)
        }.toTypedArray()

    private fun createConsentItemListener() = object : ConsentItemListener {
        override fun onConsentChange(itemIndex: Int, consent: Boolean) {
            grantResults[itemIndex] = consent
            updateConfirmButton()
        }
    }

    interface ResultListener {
        fun onResult(consentKeys: Array<String>, grantResults: BooleanArray)
    }

}