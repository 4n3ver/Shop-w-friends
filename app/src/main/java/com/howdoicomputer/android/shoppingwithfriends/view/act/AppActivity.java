package com.howdoicomputer.android.shoppingwithfriends.view.act;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
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


public class AppActivity extends ActionBarActivity
        implements MainView, OnMapReadyCallback, AppStateListener, ViewObjectUtil,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private User                currentUser;
    private ProgressDialog      mConnProgressDialog;
    private AlertDialog.Builder mErrorDialog;
    private GoogleApiClient     mGoogleApiClient;
    private Location            mLastLocation;
    private Toolbar             actionBar;
    private NavDrawerFragment   navigationBar;

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
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    /**
     * returns last known location of application
     *
     * @return location
     */
    public Location getLocation() {
        return mLastLocation;
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
}
