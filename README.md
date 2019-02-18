# Consent SDK for Android

Obtaining explicit user consent with gathering analytics data in an app, or with processing user’s personal data is important part of establishing user trust and seamless user experience.

Although implementing some dialog to obtain user consents and store them for further reference seems pretty straightforward, digging into it reveals (as usual with “simple tasks”) many programming and design details that must be implemented, which are not the core functionality of your app.

So why not use or reuse some ready-made SDK?

<img src="screenshots/consent_activity.png" width="300"/> <img src="screenshots/consent_dialog.png" width="300"/> 

## Consent SDK main functionality

- Provides configurable __consent form__ that can be displayed as:
  - __Dialog__
  - __FragmentDialog__(persists orientation changes)
  - __Activity__
- Stores consent grant results and provides access methods.

## Instalation

todo

## How to use

Firstly you need to instantiate `ConsentSDK` with `applicationContext`.

```
val consentSDK = ConsentSDK(application)
```

This object is gonna be used for all interaction with ConsentSDK.

### Consent form data

Before you can display consent form you need to prepare consent form data.

```
    private fun prepareConsentFormData(): ConsentFormData {
        return ConsentFormData(
            titleText = getString(R.string.consent_form_title),
            descriptionText = getString(R.string.consent_form_description),
            confirmButtonText = getString(R.string.consent_form_confirm_button_text),
            consentFormItems = prepareConsentFormItems())
    }

```

 create array of consentFormItems

