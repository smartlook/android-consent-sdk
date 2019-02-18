package com.smartlook.consentsdk.helpers

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.SwitchCompat
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

    fun setSwitchColor(switch: SwitchCompat, accentColor: Int) {
        val states = arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked))
        val thumbColors = intArrayOf(Color.LTGRAY, accentColor)
        val trackColors = intArrayOf(darken(Color.LTGRAY, 0.2), lighten(accentColor, 0.6))

        DrawableCompat.setTintList(DrawableCompat.wrap(switch.thumbDrawable), ColorStateList(states, thumbColors))
        DrawableCompat.setTintList(DrawableCompat.wrap(switch.trackDrawable), ColorStateList(states, trackColors))
    }

    fun darken(color: Int, fraction: Double): Int {
        val red = darkenColor(Color.red(color), fraction)
        val green = darkenColor(Color.green(color), fraction)
        val blue = darkenColor(Color.blue(color), fraction)
        return Color.argb(Color.alpha(color), red, green, blue)
    }

    fun lighten(color: Int, fraction: Double): Int {
        val red = lightenColor(Color.red(color), fraction)
        val green = lightenColor(Color.green(color), fraction)
        val blue = lightenColor(Color.blue(color), fraction)
        return Color.argb(Color.alpha(color), red, green, blue)
    }

    private fun darkenColor(color: Int, fraction: Double): Int {
        return Math.max(color - color * fraction, 0.0).toInt()
    }

    private fun lightenColor(color: Int, fraction: Double): Int {
        return Math.min(color + color * fraction, 255.0).toInt()
    }



}