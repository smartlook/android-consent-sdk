package com.smartlook.consentsdk.ui.consent

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.smartlook.consentsdk.R
import com.smartlook.consentsdk.data.ConsentFormItem
import com.smartlook.consentsdk.helpers.UtilsHelper
import com.smartlook.consentsdk.listeners.ConsentItemListener
import kotlinx.android.synthetic.main.consent_item.view.*

class ConsentItemView : LinearLayout {

    constructor(context: Context) : super(context) {
        initialize()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initialize()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        initialize()
    }

    private lateinit var layoutView: View

    private fun initialize() {
        layoutView = LayoutInflater.from(context).inflate(R.layout.consent_item, this, true)
    }

    fun setData(grantResult: Boolean, consentFormItem: ConsentFormItem) {
        consent_item_switch.text = consentFormItem.text
        consent_item_switch.isChecked = grantResult

       UtilsHelper.hideViewIfNull(consentFormItem.link, consent_item_link)
        consent_item_link.setOnClickListener {
            UtilsHelper.openLink(context, consentFormItem.link ?: return@setOnClickListener)
        }
    }

    fun registerListener(itemIndex: Int, consentItemListener: ConsentItemListener) {
        with(layoutView.consent_item_switch) {
            setOnCheckedChangeListener(android.widget.CompoundButton.OnCheckedChangeListener { _, isChecked ->
                consentItemListener.onConsentChange(itemIndex, isChecked)
            })
        }
    }
}
