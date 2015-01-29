package com.howdoicomputer.android.shoppingwithfriends;

/**
 * Created by fnurichard on 1/29/15.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
    private Button mLoginButton;
    private Button mCreateAccButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText usrNameText = (EditText) findViewById(R.id.usrName_text);
        final EditText passwordText = (EditText) findViewById(R.id.password_text);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start LoginActivity
                Intent i = new Intent(LoginActivity.this, AppActivity.class);
                if (usrNameText.getText().toString().equals("user") && passwordText.getText().toString().equals("pass")) {
                    startActivity(i);
                } else {
                    Toast.makeText(LoginActivity.this, R.string.invalid_pass, Toast.LENGTH_SHORT).show();
                }
            }
        });
        mCreateAccButton = (Button)findViewById(R.id.createAcc_button);
        mCreateAccButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start LoginActivity
                Intent i = new Intent(LoginActivity.this, AppActivity.class);
                startActivity(i);
            }
        });
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

