package com.smartlook.consentsdk.data

import java.io.Serializable

data class ConsentFormItem(val key: String,
                           val required: Boolean,
                           val text: String,
                           val link: String?) : Serializable