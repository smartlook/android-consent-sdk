package com.smartlook.consentsdksample.ui.main

import android.support.v4.text.HtmlCompat

fun String.fromHtml() = HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)