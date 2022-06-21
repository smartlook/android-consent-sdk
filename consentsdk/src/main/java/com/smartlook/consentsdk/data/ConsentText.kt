package com.smartlook.consentsdk.data

import android.text.SpannableStringBuilder
import android.text.Spanned

class ConsentText(val formattedText: Spanned) {
    constructor(text: String) : this(SpannableStringBuilder(text))
}

fun String.toConsentText() = ConsentText(this)
fun Spanned.toConsentText() = ConsentText(this)