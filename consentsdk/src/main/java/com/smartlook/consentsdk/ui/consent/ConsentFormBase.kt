package com.smartlook.consentsdk.ui.consent

import android.content.ContextWrapper
import android.support.annotation.ColorInt
import android.support.annotation.StyleRes
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.smartlook.consentsdk.ConsentSDK
import com.smartlook.consentsdk.R
import com.smartlook.consentsdk.data.ConsentFormData
import com.smartlook.consentsdk.data.ConsentFormItem
import com.smartlook.consentsdk.helpers.UtilsHelper
import com.smartlook.consentsdk.listeners.ConsentItemListener

class ConsentFormBase(
        private val consentFormData: ConsentFormData,
        private val rootView: View,
        private val resultListener: ResultListener,
        consentResults: HashMap<String, Boolean>? = null,
        @StyleRes private val styleId: Int? = null) : ContextWrapper(rootView.context) {

    private val consentApi = ConsentSDK(this)
    var consentResults: HashMap<String, Boolean>

    // We are doing it oldschool because it works for both dialog and activity
    private val tvTitle = rootView.findViewById<TextView>(R.id.consent_title)
    private val tvDescription = rootView.findViewById<TextView>(R.id.consent_description)
    private val lvConsentItemsRoot = rootView.findViewById<LinearLayout>(R.id.consent_items_root)
    private val bConfirm = rootView.findViewById<Button>(R.id.consent_confirm_button)

    init {
        this.consentResults = consentResults
                ?: obtainConsentResults(consentFormData.consentFormItems)
    }

    fun displayConsent() {
        displayTexts()
        displayConsentItems()

        updateConfirmButton()
        handleConfirmButton()

        applyFormStyle(parseOutFormStyleValues())
    }

    private fun updateConfirmButton() {
        var enable = true

        consentFormData.consentFormItems.forEachIndexed { index, item ->
            if (item.required && consentResults[keyOnIndex(index)] != true) {
                enable = false
            }
        }

        bConfirm.isEnabled = enable
    }

    private fun displayTexts() {
        with(consentFormData) {
            tvTitle.text = titleText
            tvDescription.text = descriptionText
            bConfirm.text = confirmButtonText
        }
    }

    // recycler view should have nested scroll
    private fun displayConsentItems() {
        consentFormData.consentFormItems.forEachIndexed { index, consentItem ->
            addDivider()
            lvConsentItemsRoot.addView(ConsentFormItemView(this).apply {
                setData(consentResults[keyOnIndex(index)] ?: false, consentItem)
                registerListener(index, createConsentItemListener())
            })
        }
        addDivider()
    }

    private fun addDivider() {
        lvConsentItemsRoot.addView(View(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    UtilsHelper.convertDpToPixel(this@ConsentFormBase, 1f).toInt()
            )
            background = ContextCompat.getDrawable(this@ConsentFormBase, R.color.consent_form_divider_color)
        })
    }

    private fun handleConfirmButton() {
        bConfirm.setOnClickListener {
            storeGrantResults()
            resultListener.onResult(consentResults)
        }
    }

    private fun storeGrantResults() {
        consentApi.setConsentResultsStored()

        for (entry in consentResults.entries) {
            consentApi.saveConsentResult(entry.key, entry.value)
        }
    }

    private fun obtainConsentResults(consentFormItems: Array<ConsentFormItem>) =
            hashMapOf<String, Boolean>().apply {
                consentFormItems.forEach {
                    put(it.consentKey, consentApi.loadConsentResult(it.consentKey) ?: false)
                }
            }

    private fun createConsentItemListener() = object : ConsentItemListener {
        override fun onConsentChange(itemIndex: Int, consent: Boolean) {
            consentResults[keyOnIndex(itemIndex)] = consent
            updateConfirmButton()
        }
    }

    private fun keyOnIndex(itemIndex: Int) = consentFormData.consentFormItems[itemIndex].consentKey

    private fun parseOutFormStyleValues(): StyleValues? {
        val styleValues = StyleValues()

        with(obtainStyledAttributes(styleId ?: return null, R.styleable.ConsentForm)) {
            (0 until indexCount).map { getIndex(it) }.forEach { index ->
                when (index) {
                    R.styleable.ConsentForm_cf_textColor -> styleValues.textColor = getColor(index,
                            ContextCompat.getColor(this@ConsentFormBase, R.color.consent_form_text_color))
                    R.styleable.ConsentForm_cf_titleTextColor -> styleValues.titleTextColor = getColor(index,
                            ContextCompat.getColor(this@ConsentFormBase, R.color.consent_form_title_text_color))
                    R.styleable.ConsentForm_cf_confirmButtonTextColor -> styleValues.confirmButtonTextColor = getColor(index,
                            ContextCompat.getColor(this@ConsentFormBase, R.color.consent_form_confirm_button_text_color))
                    R.styleable.ConsentForm_cf_backgroundColor -> styleValues.backgroundColor = getColor(index,
                            ContextCompat.getColor(this@ConsentFormBase, R.color.consent_form_background))
                    R.styleable.ConsentForm_cf_dividerColor -> styleValues.dividerColor = getColor(index,
                            ContextCompat.getColor(this@ConsentFormBase, R.color.consent_form_divider_color))
                }
            }
        }

        return styleValues
    }

    private fun applyFormStyle(styleValues: StyleValues?) {
        styleValues ?: return

        with(styleValues) {
            textColor?.let { tvDescription.setTextColor(it) }
            titleTextColor?.let { tvTitle.setTextColor(it) }
            backgroundColor?.let { rootView.setBackgroundColor(it) }
            confirmButtonTextColor?.let { bConfirm.setTextColor(it) }

            applyFormItemsStyle(this)
        }
    }

    private fun applyFormItemsStyle(styleValues: StyleValues) {
        (0 until lvConsentItemsRoot.childCount).map { lvConsentItemsRoot.getChildAt(it) }.forEach { view ->
            if (view is ConsentFormItemView) {
                styleValues.textColor?.let { view.setTextColor(it) }
            } else { // divider
                styleValues.dividerColor?.let { view.setBackgroundColor(it) }
            }
        }
    }

    interface ResultListener {
        fun onResult(consentResults: HashMap<String, Boolean>)
    }

    data class StyleValues(@ColorInt var textColor: Int? = null,
                           @ColorInt var titleTextColor: Int? = null,
                           @ColorInt var confirmButtonTextColor: Int? = null,
                           @ColorInt var backgroundColor: Int? = null,
                           @ColorInt var dividerColor: Int? = null)

}