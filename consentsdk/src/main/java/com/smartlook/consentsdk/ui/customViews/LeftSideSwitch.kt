package com.smartlook.consentsdk.ui.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import android.widget.LinearLayout
import com.smartlook.consentsdk.R
import kotlinx.android.synthetic.main.left_side_switch.view.*

class LeftSideSwitch : LinearLayout {

    constructor(context: Context) : super(context) {
        initialize()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initialize()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        initialize()
    }

    var text: String = ""
        set(value) {
            left_side_switch_text.text = value
            field = value
        }

    var isChecked = false
        set(value) {
            left_side_switch_switch.isChecked = value
            field = value
        }

    private var layoutView: View? = null
    private var listener: CompoundButton.OnCheckedChangeListener? = null

    fun setOnCheckedChangeListener(onCheckedChangeListener: CompoundButton.OnCheckedChangeListener) {
        this.listener = onCheckedChangeListener
    }

    private fun initialize() {
        layoutView = LayoutInflater.from(context).inflate(R.layout.left_side_switch, this, true)

        left_side_switch.setOnClickListener {
            isChecked = !isChecked
            left_side_switch_switch.isChecked = isChecked

            listener?.onCheckedChanged(left_side_switch_switch, isChecked)
        }
    }
}
