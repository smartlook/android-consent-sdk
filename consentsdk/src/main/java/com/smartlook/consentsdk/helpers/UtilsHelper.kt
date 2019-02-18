package com.smartlook.consentsdk.helpers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View

object UtilsHelper {

    fun hideViewIfNull(nullableObject: Any?, view: View) {
        view.visibility = if (nullableObject == null) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    fun openLink(context: Context, link: String) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }

}