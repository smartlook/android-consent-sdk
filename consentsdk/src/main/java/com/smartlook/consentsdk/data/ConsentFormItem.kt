package com.smartlook.consentsdk.data

import java.io.Serializable

data class ConsentFormItem(val consentKey: String,
                           val required: Boolean,
                           val text: String,
                           val link: String?) : Serializable