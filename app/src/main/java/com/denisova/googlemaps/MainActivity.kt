package com.denisova.googlemaps

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient

class MainActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {
    lateinit var mMap: GoogleMap
    private var isoffed = false;
    private lateinit var locationManager: LocationManager
    lateinit var speedtext:TextView
    private lateinit var placesClient: PlacesClient
    private lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "")
        }
        placesClient = Places.createClient(this)

        var mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        speedtext = findViewById(R.id.speedtext)
        searchEditText = findViewById(R.id.search_edit)

        setupSearch()

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
        else {
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,this)
        }

    }

    private fun setupSearch() {
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                performSearch(searchEditText.text.toString())
                true
            } else {
                false
            }
        }
    }

    private fun performSearch(query: String) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                if (response.autocompletePredictions.isNotEmpty()) {
                    val firstResult = response.autocompletePredictions[0]
                    val placeId = firstResult.placeId

                    fetchPlaceDetails(placeId)
                } else {
                    Toast.makeText(this, "Место не найдено", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Ошибка поиска: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchPlaceDetails(placeId: String) {
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        val fetchPlaceRequest = com.google.android.libraries.places.api.net.FetchPlaceRequest.builder(placeId, placeFields).build()

        placesClient.fetchPlace(fetchPlaceRequest)
            .addOnSuccessListener { response ->
                val place = response.place
                val latLng = place.latLng
                if (latLng != null) {
                    mMap.clear()
                    mMap.addMarker(MarkerOptions().position(latLng).title(place.name))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Ошибка получения данных: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        var point = LatLng(57.155461, 65.535104)
        mMap.addMarker(MarkerOptions().position(point).title("Freska"))
        mMap.isBuildingsEnabled = true
        mMap.isIndoorEnabled = true
        mMap.moveCamera(CameraUpdateFactory.newLatLng(point))
        var trafficonoff:ImageView = findViewById(R.id.trafficonoff)
        mMap.isMyLocationEnabled = true;
        trafficonoff.setOnClickListener{
            if(!isoffed){
                mMap.setTrafficEnabled(true);
                trafficonoff.setImageResource(R.drawable.trafficon)
                isoffed = true
                Log.d("Traffic", "Трафик включен")
            }
            else {
                mMap.setTrafficEnabled(false);
                trafficonoff.setImageResource(R.drawable.trafficoff)
                isoffed = false
                Log.d("Traffic", "Трафик отключен")
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        var speed = location.speed
        speedtext.text = ((speed*3.6)+0.5).toInt().toString()
    }
}