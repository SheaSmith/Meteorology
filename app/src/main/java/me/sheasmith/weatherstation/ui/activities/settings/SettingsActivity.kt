package me.sheasmith.weatherstation.ui.activities.settings

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import kotlinx.android.synthetic.main.activity_settings.*
import me.sheasmith.weatherstation.ApiManager
import me.sheasmith.weatherstation.R
import me.sheasmith.weatherstation.helpers.FormatHelper
import me.sheasmith.weatherstation.helpers.PreferencesHelper
import me.sheasmith.weatherstation.models.ApiResponse
import me.sheasmith.weatherstation.models.CurrentConditions
import me.sheasmith.weatherstation.models.UnauthorisedException
import me.sheasmith.weatherstation.models.UnitsSystem

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = 0x00000000 // transparent
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            window.addFlags(flags)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }

        swipeRefresh.setOnRefreshListener {
            doRequest()
        }

        doRequest()

        val unitsSystems = arrayListOf(UnitsSystem.METRIC, UnitsSystem.IMPERIAL, UnitsSystem.UK_HYBRID, UnitsSystem.METRIC_SI)

        changeUnitsSystem.setOnClickListener {
            AlertDialog.Builder(this)
                    .setTitle("Change Units System")
                    .setItems(unitsSystems.map { it.displayName }.toTypedArray()) { _, index ->
                        PreferencesHelper.setUnitsSystem(this, unitsSystems[index])
                        runOnUiThread {
                            unitsSystem.text = unitsSystems[index].displayName
                        }
                    }
                    .create()
                    .show()
        }

        changeHemisphereButton.setOnClickListener {
            AlertDialog.Builder(this)
                    .setTitle("Change Hemisphere")
                    .setItems(arrayOf("Northern Hemisphere", "Southern Hemisphere")) { _, index ->
                        PreferencesHelper.setSouthernHemisphere(this, index == 1)
                        runOnUiThread {
                            hemisphere.text = if (PreferencesHelper.isSouthernHemisphere(this@SettingsActivity)) "Southern" else "Northern"
                        }
                    }
                    .create()
                    .show()
        }

        changeApiKeyButton.setOnClickListener {
            startActivityForResult(Intent(this, ApiKeyActivity::class.java), 1)
        }

        back.setOnClickListener {
            finish()
        }

        changePws.setOnClickListener {
            startActivityForResult(Intent(this, LocationSearchActivity::class.java), 1)
        }
    }

    private fun doRequest() {
        ApiManager.getCurrentConditions(object : ApiResponse<CurrentConditions?> {
            override fun success(currentConditions: CurrentConditions) {
                runOnUiThread {
                    currentlySelectedPws.text = currentConditions.stationId
                    neighbourhood.text = currentConditions.neighbourhood
                    country.text = currentConditions.country
                    location.text = String.format("%f, %f", currentConditions.latitude, currentConditions.longitude)
                    elevation.text = FormatHelper.formatElevation(currentConditions.elevation, PreferencesHelper.getUnitsSystem(this@SettingsActivity))
                    software.text = currentConditions.softwareType

                    unitsSystem.text = PreferencesHelper.getUnitsSystem(this@SettingsActivity).displayName
                    hemisphere.text = if (PreferencesHelper.isSouthernHemisphere(this@SettingsActivity)) "Southern" else "Northern"
                    apiKey.text = String.format("%s•••••••••••••••••••••••••••••", PreferencesHelper.getApiKey(this@SettingsActivity).substring(0, 4))

                    loader.visibility = View.GONE
                    scrollView.visibility = View.VISIBLE

                    swipeRefresh.isRefreshing = false
                }
            }

            override fun error(e: Exception?) {
                runOnUiThread {
                    swipeRefresh.isRefreshing = false

                    if (e is UnauthorisedException) {
                        AlertDialog.Builder(this@SettingsActivity)
                                .setTitle("Invalid API Key")
                                .setMessage("It appears as if your API Key is invalid")
                                .setPositiveButton("Fix") { _, _ ->
                                    startActivityForResult(Intent(this@SettingsActivity, ApiKeyActivity::class.java), 1)
                                }
                                .setNegativeButton(android.R.string.cancel, null)
                                .create()
                                .show()
                    } else {

                        AlertDialog.Builder(this@SettingsActivity)
                                .setTitle("No Internet")
                                .setMessage("You do not appear to be connected to the internet. Please check your connection and try again.")
                                .setPositiveButton("Retry") { _, _ ->
                                    swipeRefresh.isRefreshing = true
                                    doRequest()
                                }
                                .setNegativeButton(android.R.string.cancel, null)
                                .create()
                                .show()
                    }
                }
            }
        }, this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { _, insets ->
            if (insets.systemWindowInsetTop != 0)
                mainView.setPadding(insets.systemWindowInsetLeft, insets.systemWindowInsetTop, insets.systemWindowInsetRight, insets.systemWindowInsetBottom)
            insets.consumeSystemWindowInsets()
            insets
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == 1) {
            apiKey.text = data?.getStringExtra("apiKey")
        } else if (requestCode == 2 && resultCode == 1) {
            swipeRefresh.isRefreshing = true
            doRequest()
        }
    }
}