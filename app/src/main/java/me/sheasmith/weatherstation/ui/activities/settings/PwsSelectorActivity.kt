package me.sheasmith.weatherstation.ui.activities.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_pws_selector.*
import kotlinx.android.synthetic.main.activity_pws_selector.rootView
import kotlinx.android.synthetic.main.activity_settings.currentlySelectedPws
import me.sheasmith.weatherstation.ApiManager
import me.sheasmith.weatherstation.R
import me.sheasmith.weatherstation.helpers.PreferencesHelper
import me.sheasmith.weatherstation.models.ApiResponse
import me.sheasmith.weatherstation.models.Station
import me.sheasmith.weatherstation.models.UnauthorisedException

class PwsSelectorActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    private lateinit var stations: List<Station>
    private var oneLoaded = false

    private var currentlySelectedStation: Station? = null

    private var geocode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pws_selector)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        geocode = intent.getStringExtra("geocode")

        doRequest()

        done.setOnClickListener {

            val intent = Intent()
            intent.putExtra("hadPws", PreferencesHelper.getPws(this) != null)

            PreferencesHelper.setPws(this, currentlySelectedStation?.stationId)
            PreferencesHelper.setPwsLocation(this, String.format("%f,%f", currentlySelectedStation?.latitude, currentlySelectedStation?.longitude))

            setResult(1, intent)
            finish()
        }
    }

    private fun doRequest() {
        progressBar.visibility = View.VISIBLE
        map.view?.visibility = View.GONE

        ApiManager.findNear(object : ApiResponse<List<Station>> {
            override fun success(value: List<Station>) {
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    map.view?.visibility = View.VISIBLE

                    stations = value

                    if (oneLoaded)
                        showPins()

                    oneLoaded = true
                }
            }

            override fun error(e: Exception?) {
                runOnUiThread {
                    if (e is UnauthorisedException) {
                        AlertDialog.Builder(this@PwsSelectorActivity)
                                .setTitle("Invalid API Key")
                                .setMessage("It appears as if your API Key is invalid")
                                .setPositiveButton("Fix") { _, _ ->
                                    startActivityForResult(Intent(this@PwsSelectorActivity, ApiKeyActivity::class.java), 1)
                                }
                                .setNegativeButton(android.R.string.cancel, null)
                                .create()
                                .show()
                    } else {

                        AlertDialog.Builder(this@PwsSelectorActivity)
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
        }, geocode, this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (oneLoaded)
            showPins()

        oneLoaded = true
    }

    fun showPins() {
        stations.forEach {
            val marker = mMap.addMarker(MarkerOptions().position(LatLng(it.latitude, it.longitude)).title(String.format("%s (%s)", it.stationName, it.stationId)))
            marker.tag = it
        }

        val geocode = intent.getStringExtra("geocode").split(",")
        val lat: Double = geocode[0].toDouble()
        val long: Double = geocode[1].toDouble()

        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat, long)))

        mMap.setOnMarkerClickListener {
            val station = it.tag as Station

            currentlySelectedPws.text = String.format("%s (%s)", station.stationName, station.stationId)
            done.isEnabled = true

            currentlySelectedStation = station

            false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1)
            doRequest()
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