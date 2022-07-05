package com.smartlook.consentsdk.data

import android.os.Parcelable
import android.text.SpannableStringBuilder
import android.text.Spanned
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize data class ConsentText(val formattedText: @RawValue Spanned) : Parcelable {
    constructor(text: String) : this(SpannableStringBuilder(text) as Spanned)
}

fun String.toConsentText() = ConsentText(this)
fun Spanned.toConsentText() = ConsentText(this)