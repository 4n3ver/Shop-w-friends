package com.howdoicomputer.android.shoppingwithfriends.act;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.howdoicomputer.android.shoppingwithfriends.R;
import com.howdoicomputer.android.shoppingwithfriends.handler.MainHandler;
import com.howdoicomputer.android.shoppingwithfriends.model.Account;
import com.howdoicomputer.android.shoppingwithfriends.model.User;
import com.howdoicomputer.android.shoppingwithfriends.model.database.Database;
import com.howdoicomputer.android.shoppingwithfriends.view.MainView;


public class AppActivity extends ActionBarActivity
        implements MainView, OnMapReadyCallback, FriendListFragment.OnFragmentInteractionListener,
        MainFeedFragment.OnFragmentInteractionListener {

    private MainHandler handler;
    private User        currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        handler = new MainHandler(this);
        currentUser = new Gson().fromJson(getIntent().getExtras().getString("Account"), User.class);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.mainFragmentContainer,
                    MainFeedFragment.newInstance(currentUser)).commit();
        }
    }

    /**
     * Unauthenticate from Firebase and from providers where necessary.
     */
    public void logout(View v) {
        handler.logout();
    }

    public void friendList(View v) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.mainFragmentContainer, FriendListFragment.newInstance(
                currentUser));
        transaction.addToBackStack(null);    // let user navigate back to previous fragment

        transaction.commit();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoggedOut() {
        Database.destroyInstance();
        finish();
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
    public void onFragmentInteraction(Uri uri) {

    }
}
