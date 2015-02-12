package com.howdoicomputer.android.shoppingwithfriends.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.howdoicomputer.android.shoppingwithfriends.handler.MainHandler;
import com.howdoicomputer.android.shoppingwithfriends.view.MainView;


public class AppActivity extends ActionBarActivity implements MainView, OnMapReadyCallback {
    private MainHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        handler = new MainHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(com.howdoicomputer.android.shoppingwithfriends.R.layout.activity_app);
    }

    /**
     * Unauthenticate from Firebase and from providers where necessary.
     */
    public void logout(View v) {
        handler.logout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater()
                .inflate(com.howdoicomputer.android.shoppingwithfriends.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.howdoicomputer.android.shoppingwithfriends.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoggedOut() {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), WelcomeAct.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
}
