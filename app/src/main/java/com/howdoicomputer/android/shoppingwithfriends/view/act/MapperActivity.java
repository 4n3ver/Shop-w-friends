package com.howdoicomputer.android.shoppingwithfriends.view.act;

import android.app.Activity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.howdoicomputer.android.shoppingwithfriends.R;

/**
 * Created by Ricardomacias on 3/25/2015.
 */
public class MapperActivity extends Activity implements OnMapReadyCallback {
    private double latitude;
    private double longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapper);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        latitude = getIntent().getExtras().getDouble("Latitude");
        longitude = getIntent().getExtras().getDouble("Longitude");
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng item = new LatLng(latitude, longitude);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(item, 13));

        map.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(item));
    }
}
