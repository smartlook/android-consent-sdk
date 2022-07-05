package com.smartlook.consentsdk.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ConsentFormItem(val consentKey: String,
                                         val required: Boolean,
                                         val description: ConsentText,
                                         val link: String?) : Parcelable