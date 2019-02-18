package com.smartlook.consentsdksample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.smartlook.consentsdk.ConsentSDK
import com.smartlook.consentsdk.data.ConsentFormData
import com.smartlook.consentsdk.data.ConsentFormItem
import com.smartlook.consentsdk.listeners.ConsentResultListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ConsentResultListener {

    private lateinit var consentSDK: ConsentSDK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        consentSDK = ConsentSDK(this)

        val consentFormData = sampleConsentFormData()

        show_dialog.setOnClickListener {
            consentSDK.showConsentFormDialogFragment(this, consentFormData)
        }

        show_dialog_fragment.setOnClickListener {
            consentSDK.showConsentFormDialogFragment(this, consentFormData)
        }

        start_activity.setOnClickListener {
            consentSDK.startConsentFormActivity(this, consentFormData, 10000)
        }
    }

    override fun onConsentResult(consentResults: HashMap<String, Boolean>) {
        Toast.makeText(this, "Result obtained", Toast.LENGTH_LONG).show()
    }

    private fun sampleConsentFormData(): ConsentFormData {
        val consentItems = sampleConsentFormItem()

        return ConsentFormData(
            titleText = "This is a ConsentFormData dialog",
            descriptionText = "Curabitur sagittis hendrerit ante. Aenean fermentum risus id tortor. Integer in sapien. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Aliquam ornare wisi eu metus. Etiam dui sem, fermentum vitae, sagittis id, malesuada in, quam.",
            confirmButtonText = "This is my will",
            consentFormItems = consentItems)
    }

    private fun sampleConsentFormItem(): Array<ConsentFormItem> {
        return arrayOf(
            ConsentFormItem(
                "AGE_CONSENT",
                true,
                "I certify that I'm over the age of fifteen, have read, understood adn accepted Privacy Policy.",
                null
            ),
            ConsentFormItem(
                "SDK_CONSENT",
                false,
                "I agree to play for free and that my personal data is collected via the SDK tools build into the application.",
                "https://www.nplix.com/kotlin-parcelable-array-objects/"
            )
        )
    }
}
