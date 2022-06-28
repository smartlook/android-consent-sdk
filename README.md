# Consent SDK for Android

Obtaining explicit user consent regarding the gathering analytics data in an app, or with processing user’s personal data is an important part of establishing user trust and seamless user experience.

Although implementing some form to obtain user consents and store them for further reference seems pretty straightforward, digging into it reveals (as usual with “simple tasks”) many programming and design details that must be implemented, which are not the core functionality of your app.

<img src="screenshots/consent_form_activity.png" width="300"/> <img src="screenshots/consent_form_dialog.png" width="300"/> 

## Consent SDK main functionality

- Provides configurable __consent form__ that can be displayed as:
  - __Dialog__
  - __FragmentDialog__(persists orientation changes)
  - __Activity__
  - __Fragment__
- Stores consent results and provides access methods.

## Installation
Add the following dependency in your app's build.gradle:

```
implementation 'com.smartlook:consent:1.0'
```

And add the following in your project's build.gradle:

```
allprojects {
    repositories {
        maven {
            url "https://sdk.smartlook.com/android/release"
        }
    }
}
```

## How to use

Firstly you need to instantiate `ConsentSDK` with `applicationContext`.

```
val consentSDK = ConsentSDK(applicationContext)
```

This object is going to be used for all interactions with ConsentSDK.

### Consent form data

Before you can display consent form you need to prepare consent form data.

```
companion object {
    const val CONSENT_1_KEY = "consent_1_key"
    const val CONSENT_2_KEY = "consent_2_key"
}

...

val consentFormItems = arrayOf(
    ConsentFormItem(
        consentKey = CONSENT_1_KEY,
        required = true,
        description = getString(R.string.consent_1_description),
        link = null
    ),
    ConsentFormItem(
        consentKey = CONSENT_2_KEY,
        required = false,
        description = getString(R.string.consent_2_description),
        link = getString(R.string.consent_2_link)
    )
)

val consentFormData = ConsentFormData(
    titleText = getString(R.string.consent_form_title),
    descriptionText = getString(R.string.consent_form_description),
    confirmButtonText = getString(R.string.consent_form_confirm_button_text),
    consentFormItems = consentFormItems)

```

Array `consentFormItems` represents consents we want the user to grant us. Every item needs to have:
 - unique `consentKey` that represents it and can be used to obtain grant result for this consent.
 - `required` flag. If this flag is set to `true` user cannot successfully finish the consent form without granting this consent.
 - `descriptionText` informing the user about the consent.
 - `link` (optional) that lets the user open a web page (URL) with more info.
 
 Object `consentFormData` provides all needed data for displaying consent form.

### Consent form display options

**Optionally** you can customize the display options for your consent form dialog/dialogfragment/fragment/activity by passing a `ConsentFormDisplayOptions` object into your showConsentForm function call.<br><br>
With the `consentFormDescriptionScrollingOnly` flag, you can make the whole layout scrollable or only title & description.<br>
By default `consentFormDescriptionScrollingOnly` is set to `true`.<br>

Usage example:
```
val consentFormDisplayOptions = ConsentFormDisplayOptions(consentFormDescriptionScrollingOnly = false)

consentSDK.showConsentFormDialogFragment(
    this,
    consentFormData = consentFormData,
    consentFormDisplayOptions = consentFormDisplayOptions,
    styleId = R.style.DialogStyle
)
```

### Showing consent form on `Dialog`
A most simple and straight-forward way of displaying consent form is on `Dialog`. It has one __drawback__, this way we __cannot__ properly persist user data on orientation change. Use this if you have locked screen orientation.

```
consentSDK.showConsentFormDialog(consentFormData, object : ConsentResultsListener {
    override fun onConsentResults(consentResults: HashMap<String, Boolean>) {
        // consent form result here
    }
})
```

### Showing consent form on `DialogFragment`
By using `DialogFragment` SDK can properly handle orientation changes.

```
consentSDK.showConsentFormDialogFragment(<activity>/<fragment>, consentFormData)
```

The first parameter of `showConsentFormDialogFragment` accepts `Activity` or `Fragment` reference so you can call it from both.
Your calling `Activity` or `Fragment` __must__ implement ConsentResultsListener.

```
class SampleActivity : AppCompatActivity(), ConsentResultsListener {

...

  override fun onConsentResults(consentResults: HashMap<String, Boolean>) {
      // consent form result here
  }
}
```

### Starting consent form `Activity`

```
class SampleActivity : AppCompatActivity() {

  companion object {
      const val CONSENT_REQUEST_CODE = 10001
  }

  ...

  consentSDK.startConsentFormActivity(this, consentFormData, CONSENT_REQUEST_CODE)

  ...

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
      if (requestCode == CONSENT_REQUEST_CODE) {
          if (resultCode == Activity.RESULT_OK) {
              val consentResults = consentSDK.parseOutConsentResults(data)
          } else {
              // user didnt confirm the form (back press)
          }
      }
  }
}
```

Consent form `Activity` is started "for a result" so to get a result you need to implement `onActivityResult` method in your `Activity`. 

### Creating conset form `Fragment`
Method `createConsentFormFragment` lets you create `Fragment` with consent form. Example usage might look something like this:

```
const val TAG = "unique_fragment_tag"

...

with(supportFragmentManager) {
    beginTransaction()
        .replace(R.id.fragment_placeholder, consentSDK.createConsentFormFragment(consentFormData), TAG)
        .commit()
    executePendingTransactions()
}
```

`ConsentResultsListener` can be registered like this:

```
val consentFormFragment = supportFragmentManager.findFragmentByTag(TAG) as ConsentFormFragment
consentFormFragment.registerConsentResultsListener(object : ConsentResultsListener {
            override fun onConsentResults(consentResults: HashMap<String, Boolean>) {
                // Consent form result here
            }
        })
```

### Consent results
When user sucessfully finishes consent form you gonna get `consentResult`. It is a `HashMap<String,Boolean>` in which:
- `key` == `consentKey`
- `value` represents `consentResult`:
  - `true` consent was granted.
  - `false` consent was rejected.

### Are consent results stored?
SDK method `areConsentResultsStored()` can be used to determine if the user has already successfully filled consent form and results were stored.

### Obtaining consent

If you want to obtain a grant result for given conset (identified by unique `consentKey`) you can do it like this:

```
val consentResult = consentSDK.loadConsetResult(consentKey)
```

If `consentResult` is:
- `true` consent was granted.
- `false` consent was rejected.
- `null` not defined.

## Styling

<img src="screenshots/consent_form_activity_styled.png" width="300"/> <img src="screenshots/consent_form_dialog_styled.png" width="300"/> 

You can define custom `style` for the consent form. All configurable attributes are listed in the table below.

|         Attribute         |                    Description                   |
|:-------------------------:|:------------------------------------------------:|
| colorAccent               | Confirm button, link icons and `Switches` color. |
| cf_textColor              | Description text and form item texts color.      |
| cf_titleTextColor         | Title text color.                                |
| cf_confirmButtonTextColor | Confirm button text color.                       |
| cf_backgroundColor        | Form background color.                           |
| cf_dividerColor           | Form item list divider color.                    |

### `Dialog`/`FragmentDialog` 

In `styles.xml` define custom Dialog style:

```
<style name="DialogStyle" parent="Base.Theme.AppCompat.Light.Dialog">
    <item name="colorAccent">#35E6A5</item>
    <item name="cf_textColor">#F4F4F4</item>
    <item name="cf_titleTextColor">#F4F4F4</item>
    <item name="cf_confirmButtonTextColor">#F4F4F4</item>
    <item name="cf_backgroundColor">#26262E</item>
    <item name="cf_dividerColor">#F4F4F4</item>
</style>
```

Then add the style reference to `showConsentFormDialog`/`showConsentFormDialogFragment` method like this:

```
// Dialog
consentSDK.showConsentFormDialog(this, consentFormData, R.style.DialogStyle, listener)

// DialogFragment
consentSDK.showConsentFormDialogFragment(this, consentFormData, R.style.DialogStyle)
```

### `Activity`

In `styles.xml` define custom Activity style:

```
<style name="ActivityStyle" parent="Theme.AppCompat.Light.NoActionBar">
    <item name="colorAccent">#21A76A</item>
    <item name="cf_textColor">#F4F4F4</item>
    <item name="cf_titleTextColor">#F4F4F4</item>
    <item name="cf_confirmButtonTextColor">#F4F4F4</item>
    <item name="cf_backgroundColor">#26262E</item>
    <item name="cf_dividerColor">#F4F4F4</item>
</style>
```

Then add the style reference to `startConsentFormActivity` method like this:

```
consentSDK.startConsentFormActivity(this, consentFormData, CONSENT_REQUEST_CODE, R.style.ActivityStyle)
```

### Fragment

In `styles.xml` define custom Fragment style:

```
<style name="FragmentStyle" parent="Theme.AppCompat.Light.NoActionBar">
    <item name="colorAccent">#21A76A</item>
    <item name="cf_textColor">#F4F4F4</item>
    <item name="cf_titleTextColor">#F4F4F4</item>
    <item name="cf_confirmButtonTextColor">#F4F4F4</item>
    <item name="cf_backgroundColor">#26262E</item>
    <item name="cf_dividerColor">#F4F4F4</item>
</style>
```

Then add the style reference to `startConsentFormActivity` method like this:

```
consentSDK.createConsentFormFragment(consentFormData, R.style.FragmentStyle)
```
