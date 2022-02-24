package com.example.marcismaps;

import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple demo for using google Maps. It shows how to...
 * ...generate a Map (as part of the whole screen)
 * ...set a marker
 * ...react on clicking on a marker or on the whole map
 *  To make it work, generate your own key in res/values/google_maps_api.xml The file has the following structure:
 *  <resources>
 *     <!--
 *     TODO: Before you run your application, you need a Google Maps API key.
 *
 *     To get one, follow this link, follow the directions and press "Create" at the end:
 *
 *     https://console.developers.google.com/flows/enableapi?apiid=maps_android_backend&keyType=CLIENT_SIDE_ANDROID&r=5B:EA:4C:AF:07:45:CD:3C:4A:D9:AD:40:51:0F:83:B0:56:A1:C3:4D%3Bat.ac.htlinn.androidexamples.googlemaps
 *
 *     You can also add your credentials to an existing key, using these values:
 *
 *     Package name:
 *     at.ac.htlinn.androidexamples.googlemaps
 *
 *     SHA-1 certificate fingerprint:
 *     5B:EA:4C:AF:07:45:CD:3C:4A:D9:AD:40:51:0F:83:B0:56:A1:C3:4D
 *
 *     Alternatively, follow the directions here:
 *     https://developers.google.com/maps/documentation/android/start#get-key
 *
 *     Once you have your key (it starts with "AIza"), replace the "google_maps_key"
 *     string in this file.
 *     -->
 *     <string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">ADD YOUR KEY HERE</string>
 * </resources>
 *
 */
public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private TextView feedback;
    private int markerCnt = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_simple_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        feedback = findViewById(R.id.feedback);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Innsbruck, Austria.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Innsbruck and move the camera
        LatLng ibk = new LatLng(47.259659,11.400375);
        mMap.addMarker(new MarkerOptions().position(ibk).title("Marker in Innsbruck"));


        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(ibk));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ibk,10)); //Values from 2 to 21 possible
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);

    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        feedback.setText(marker.getTitle());
        return true; //event is consumed
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.addMarker(new MarkerOptions().position(latLng).title(String.format("Marker %d", ++markerCnt)));
        feedback.setText("New Marker created!");
    }
}