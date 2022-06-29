package com.smartlook.consentsdk.data

import java.io.Serializable

data class ConsentFormItem(val consentKey: String,
                           val required: Boolean,
                           val description: ConsentText,
                           val link: String?) : Serializable