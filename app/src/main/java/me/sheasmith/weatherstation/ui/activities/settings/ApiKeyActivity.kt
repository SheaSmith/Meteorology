package me.sheasmith.weatherstation.ui.activities.settings

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import kotlinx.android.synthetic.main.activity_api_key.*
import me.sheasmith.weatherstation.ApiManager
import me.sheasmith.weatherstation.R
import me.sheasmith.weatherstation.helpers.PreferencesHelper
import me.sheasmith.weatherstation.helpers.ProgressHelper
import me.sheasmith.weatherstation.models.ApiResponse
import me.sheasmith.weatherstation.models.Station
import me.sheasmith.weatherstation.models.UnauthorisedException

class ApiKeyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_key)

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

        if (PreferencesHelper.getApiKey(this) == null) {
            back.visibility = View.GONE
        }

        back.setOnClickListener { finish() }

        save.setOnClickListener {
            val dialog = ProgressHelper.showDialog(this, "Validating...")

            ApiManager.findNear(object : ApiResponse<List<Station>> {
                override fun success(value: List<Station>) {
                    runOnUiThread {
                        dialog.dismiss()
                    }
                    PreferencesHelper.setApiKey(this@ApiKeyActivity, apiKey.text.toString())

                    if (PreferencesHelper.getPws(this@ApiKeyActivity) == null) {
                        startActivity(Intent(this@ApiKeyActivity, LocationSearchActivity::class.java))
                        finish()
                    } else {
                        val intent = Intent()
                        intent.putExtra("apiKey", apiKey.text.toString())
                        setResult(1, intent)
                        finish()
                    }
                }

                override fun error(e: Exception?) {
                    runOnUiThread {
                        dialog.dismiss()

                        if (e is UnauthorisedException) {
                            AlertDialog.Builder(this@ApiKeyActivity)
                                    .setTitle("Invalid API Key")
                                    .setMessage("The API key you entered was invalid. Please check it and try again.")
                                    .setPositiveButton(android.R.string.ok, null)
                                    .create()
                                    .show()
                        } else {
                            AlertDialog.Builder(this@ApiKeyActivity)
                                    .setTitle("No Internet")
                                    .setMessage("You do not appear to be connected to the internet. Please check your connection and try again.")
                                    .setPositiveButton(android.R.string.ok, null)
                                    .create()
                                    .show()
                        }
                    }
                }
            }, "", this, apiKey.text.toString())
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { _, insets ->
            if (insets.systemWindowInsetTop != 0)
                rootView.setPadding(insets.systemWindowInsetLeft, insets.systemWindowInsetTop, insets.systemWindowInsetRight, insets.systemWindowInsetBottom)
            insets.consumeSystemWindowInsets()
            insets
        }
    }
}