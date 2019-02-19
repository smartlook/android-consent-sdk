package com.smartlook.consentsdksample.ui.main

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smartlook.consentsdksample.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.consent_result.view.*

class ConsentResultsAdapter(val context: Context,
                            private var consentResults: HashMap<String, Boolean?>) : RecyclerView.Adapter<ConsentResultsAdapter.ConsentResultVH>() {

    private var consentKeys = consentResults.keys.toList()

    /* Public methods *****************************************************************************/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsentResultVH {
        return ConsentResultVH(LayoutInflater.from(context).inflate(R.layout.consent_result, parent, false))
    }

    override fun onBindViewHolder(holder: ConsentResultVH, position: Int) {
        holder.bindConsentResult(consentKeys[position], consentResults[consentKeys[position]])
    }

    override fun getItemCount() = consentResults.size


    /* View holders *******************************************************************************/

    inner class ConsentResultVH(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bindConsentResult(consentKey: String, grantResult: Boolean?) {

            containerView.consent_key.text = consentKey
            containerView.grand_result.text = grantResultText(grantResult)
            containerView.grand_result.setTextColor(grantResultColor(grantResult))
        }

        private fun grantResultText(grantResult: Boolean?): String {
            return when(grantResult) {
                true -> "GRANTED"
                false -> "REJECTED"
                null -> "UNDEFINED"
            }
        }

        private fun grantResultColor(grantResult: Boolean?): Int {
            return when(grantResult) {
                true -> Color.GREEN
                false -> Color.RED
                null -> Color.GRAY
            }
        }
    }

}