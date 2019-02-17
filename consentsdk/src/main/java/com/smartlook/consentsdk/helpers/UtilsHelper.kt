package com.smartlook.consentsdk.helpers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import java.io.InvalidClassException

object UtilsHelper {

    fun hideViewIfNull(nullableObject: Any?, view: View) {
        view.visibility = if (nullableObject == null) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    fun stringFromResourceOrString(context: Context, resourceOrString: Any): String {
        return when (resourceOrString) {
            is String -> resourceOrString
            is Int -> context.getString(resourceOrString)
            else -> throw InvalidClassException("stringFromStringResOrString(): resourceOrString must be String or Int!")
        }
    }

    fun openLink(context: Context, link: String) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }

}