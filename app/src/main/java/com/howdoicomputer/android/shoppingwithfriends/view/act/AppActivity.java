package com.howdoicomputer.android.shoppingwithfriends.view.act;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.howdoicomputer.android.shoppingwithfriends.R;
import com.howdoicomputer.android.shoppingwithfriends.model.database.Database;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.Account;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.User;
import com.howdoicomputer.android.shoppingwithfriends.view.act.navigationdrawer.NavDrawerFragment;
import com.howdoicomputer.android.shoppingwithfriends.view.viewinterface.AppStateListener;
import com.howdoicomputer.android.shoppingwithfriends.view.viewinterface.MainView;
import com.howdoicomputer.android.shoppingwithfriends.view.viewinterface.ViewObjectUtil;

import java.util.Locale;


public class AppActivity extends ActionBarActivity
        implements MainView, OnMapReadyCallback, AppStateListener, ViewObjectUtil,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static Location            mLastLocation;
    private static Geocoder            coder;
    private static Location            location;
    private        User                currentUser;
    private        ProgressDialog      mConnProgressDialog;
    private        AlertDialog.Builder mErrorDialog;
    private        GoogleApiClient     mGoogleApiClient;
    private        Toolbar             actionBar;
    private        NavDrawerFragment   navigationBar;
    private        LocationManager     locationManager;
    private        boolean             canGetLocation;
    private        boolean             isGPSEnabled;
    private        boolean             isNetworkEnabled;

    public static Location getLastLocation() {
        return mLastLocation;
    }

    public static Geocoder getGeoCoder() {
        return coder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        Firebase.setAndroidContext(getApplicationContext());
        currentUser = new Gson().fromJson(getIntent().getExtras().getString("Account"), User.class);
        actionBar = (Toolbar) findViewById(R.id.mainActBar);
        setSupportActionBar(actionBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        navigationBar = (NavDrawerFragment) getSupportFragmentManager().findFragmentById(
                R.id.fragmentNavBar);

        navigationBar.setUp(R.id.fragmentNavBar, (DrawerLayout) findViewById(
                R.id.nav_drawer_layout), actionBar);

        if (savedInstanceState == null) {
            //            getSupportFragmentManager().beginTransaction().add(R.id
            // .mainFragmentContainer,
            //                    MainFeedFragment.newInstance(currentUser)).commit();
        }

        /* setup the progress dialog that is displayed later when connecting to the server */
        mConnProgressDialog = new ProgressDialog(this);
        mConnProgressDialog.setCancelable(false);

        /* setup the error dialog that is displayed later when input error detected */
        mErrorDialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                .setPositiveButton(android.R.string.ok, null).setIcon(
                        android.R.drawable.ic_dialog_alert);

        buildGoogleApiClient();
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        coder = new Geocoder(this, Locale.getDefault());
    }

    @Override
    public double[] getLocation() {
        double[] coord = new double[2];
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(
                    LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled) {
                //TODO: PLEASE SOMEONE HANDLE ask user to activate gps or stab the user in the eye!
            } else if (!isGPSEnabled && !isNetworkEnabled) {
                //TODO: PLEASE SOMEONE HANDLE ask user to activate gps or stab the user in the eye!
            } else {
                this.canGetLocation = true;
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500000,
                                50, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(
                                    LocationManager.GPS_PROVIDER);
                        }
                    }
                }
            }
        } catch (Exception e) {
            getUiUtil().showErrorDialog(e.getLocalizedMessage());
        }
        if (location != null) {
            coord[0] = location.getLatitude();
            coord[1] = location.getLongitude();
        }
        return coord;
    }

    @Override
    public String getAddress() {
        String addr = "N/A";
        try {
            Address addsrc = getGeoCoder().getFromLocation(getLocation()[0], getLocation()[1], 1)
                    .get(0);
            addr = addsrc.getAddressLine(0);
        } catch (Exception e) {
            getUiUtil().showErrorDialog(e.getLocalizedMessage());
        }
        return addr;
    }


    /**
     * Creates instance of the google api client to be able to find
     * current location of the user.
     */

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Database.destroyInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.howdoicomputer.android.shoppingwithfriends.R.menu.menu_main,
                menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoggedOut() {
        Intent mainApp = new Intent();
        mainApp.setClass(getApplicationContext(), WelcomeAct.class);
        startActivity(mainApp);
        finish();
    }

    @Override
    public Account getLatestAccount() {
        return currentUser;
    }

    @Override
    public AppStateListener getAppStateListener() {
        return this;
    }

    @Override
    public ViewObjectUtil getUiUtil() {
        return this;
    }

    @Override
    public void refreshView() {

    }

    @Override
    public void onAccountChanged(Account changedAccount) {
        currentUser = (User) changedAccount;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void showErrorDialog(final String message) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mErrorDialog.setTitle("Error").setMessage(message).show();
            }
        }, 500);
    }

    @Override
    public void showProgressDialog(final String title, final String message) {
        mConnProgressDialog.setTitle(title);
        mConnProgressDialog.setMessage(message);
        mConnProgressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mConnProgressDialog.hide();
            }
        }, 500);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        navigationBar.onBackPressed();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}