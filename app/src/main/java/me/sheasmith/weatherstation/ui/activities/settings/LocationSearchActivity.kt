package me.sheasmith.weatherstation.ui.activities.settings

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import kotlinx.android.synthetic.main.activity_location_search.*
import me.sheasmith.weatherstation.ApiManager
import me.sheasmith.weatherstation.R
import me.sheasmith.weatherstation.helpers.PreferencesHelper
import me.sheasmith.weatherstation.models.ApiResponse
import me.sheasmith.weatherstation.models.Location
import me.sheasmith.weatherstation.models.UnauthorisedException
import me.sheasmith.weatherstation.ui.activities.CurrentActivity

class LocationSearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_search)

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

        if (PreferencesHelper.getPws(this) == null) {
            back.visibility = View.GONE
        }

        back.setOnClickListener { finish() }

        search.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            locations.visibility = View.GONE
            noResults.visibility = View.GONE

            doRequest()
        }
    }

    private fun doRequest() {
        progressBar.visibility = View.VISIBLE
        noResults.visibility = View.GONE
        locations.visibility = View.GONE

        ApiManager.searchLocation(object : ApiResponse<List<Location>> {
            override fun success(value: List<Location>) {
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    if (value.isEmpty()) {
                        noResults.visibility = View.VISIBLE
                    } else {
                        locations.visibility = View.VISIBLE
                    }

                    val adapter = ArrayAdapter(this@LocationSearchActivity, android.R.layout.simple_list_item_1, value.map { it.address })
                    locations.adapter = adapter
                    locations.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                        val location = value[position]
                        val geocode = String.format("%f,%f", location.latitude, location.longitude)
                        val intent = Intent(this@LocationSearchActivity, PwsSelectorActivity::class.java)
                        intent.putExtra("geocode", geocode)
                        startActivityForResult(intent, 1)
                    }
                }
            }

            override fun error(e: Exception?) {
                runOnUiThread {
                    if (e is UnauthorisedException) {
                        AlertDialog.Builder(this@LocationSearchActivity)
                                .setTitle("Invalid API Key")
                                .setMessage("It appears as if your API Key is invalid")
                                .setPositiveButton("Fix") { _, _ ->
                                    startActivityForResult(Intent(this@LocationSearchActivity, ApiKeyActivity::class.java), 2)
                                }
                                .setNegativeButton(android.R.string.cancel, null)
                                .create()
                                .show()
                    } else {

                        AlertDialog.Builder(this@LocationSearchActivity)
                                .setTitle("No Internet")
                                .setMessage("You do not appear to be connected to the internet. Please check your connection and try again.")
                                .setPositiveButton("Retry") { _, _ ->
                                    doRequest()
                                }
                                .setNegativeButton(android.R.string.cancel, null)
                                .create()
                                .show()
                    }
                }
            }
        }, location.text.toString(), this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == 1) {
            val hadPws = data?.getBooleanExtra("hadPws", false)

            if (hadPws!!) {
                setResult(1)
            } else {
                startActivity(Intent(this, CurrentActivity::class.java))
            }

            finish()
        } else if (requestCode == 2) {
            doRequest()
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