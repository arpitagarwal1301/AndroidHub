package com.agarwal.arpit.androidhub.googlemaps

import android.content.res.Resources
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.agarwal.arpit.androidhub.R
import com.agarwal.arpit.common.logging.HubLog
import com.agarwal.arpit.common.utils.getStringWrapper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val TAG = MapsActivity::class.java.simpleName
    private lateinit var _map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        _map = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        val bangalore = LatLng(12.922312, 77.637683)
        val zoomLevel = 15f

        _map.addMarker(MarkerOptions().position(bangalore).title("Marker in Bangalore"))
        _map.moveCamera(CameraUpdateFactory.newLatLngZoom(bangalore, zoomLevel))

        setMapLongClick(_map)
        setPoiClick(_map)
        setMapStyle(_map)

    }

    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->

            val snippetText = String.format(
                    Locale.getDefault(),
                    "Lat: %1$.5f, Long: %2$.5f",
                    latLng.latitude,
                    latLng.longitude
            )

            map.addMarker(
                    MarkerOptions()
                            .position(latLng)
                            .title(getStringWrapper(R.string.marker_title))
                            .snippet(snippetText)
            )
        }
    }

    private fun setPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener { poi ->

            TODO("Remove market if already there")
            val poiMarker = map.addMarker(
                    MarkerOptions()
                            .position(poi.latLng)
                            .title(poi.name)
            )


            poiMarker.showInfoWindow()
        }
    }

    private fun setMapStyle(map: GoogleMap) {
        try {
            // Customize the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this,
                            R.raw.map_style_retro
                    )
            )

            if (!success) {
                HubLog.e("$TAG Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            HubLog.e("$TAG Can't find style. Error: ", e)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.normal_map -> {
                _map.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.hybrid_map -> {
                _map.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            R.id.satellite_map -> {
                _map.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_map -> {
                _map.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
