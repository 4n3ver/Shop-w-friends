package com.howdoicomputer.android.shoppingwithfriends;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.Firebase;


public class AppActivity extends ActionBarActivity {
    private static Firebase mAccDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: this activity created twice somehow..
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        mAccDatabase = new Firebase("https://crackling-heat-6364.firebaseio.com/");
    }

    /**
     * Unauthenticate from Firebase and from providers where necessary.
     */
    public void logout(View v) {
        mAccDatabase.unauth();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
