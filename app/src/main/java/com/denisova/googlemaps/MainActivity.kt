package com.denisova.googlemaps

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {
    lateinit var mMap: GoogleMap
    private var isoffed = false;
    private lateinit var locationManager: LocationManager
    lateinit var speedtext:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        var mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        speedtext = findViewById(R.id.speedtext)

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
        else {
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,this)
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