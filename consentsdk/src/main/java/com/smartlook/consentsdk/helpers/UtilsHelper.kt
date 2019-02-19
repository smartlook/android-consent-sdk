package com.smartlook.consentsdk.helpers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.DisplayMetrics
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

    fun convertDpToPixel(context: Context, dp: Float): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun convertPixelsToDp(context: Context, px: Float): Float {
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

}