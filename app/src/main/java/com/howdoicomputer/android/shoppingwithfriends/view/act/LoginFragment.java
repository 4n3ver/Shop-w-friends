package com.howdoicomputer.android.shoppingwithfriends.view.act;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.howdoicomputer.android.shoppingwithfriends.R;
import com.howdoicomputer.android.shoppingwithfriends.handler.LoginHandler;
import com.howdoicomputer.android.shoppingwithfriends.view.viewinterface.WelcomeView;

/**
 * generated from template
 */
public class LoginFragment extends Fragment {
    private LoginHandler         handler;
    private AutoCompleteTextView userName;
    private EditText             password;
    private Button               login;
    private Button               cancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        userName = (AutoCompleteTextView) rootView.findViewById(R.id.frag_login_usrName_text);
        password = (EditText) rootView.findViewById(R.id.frag_login_password_text);
        login = (Button) rootView.findViewById(R.id.login_button);
        cancel = (Button) rootView.findViewById(R.id.cancelLogin_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginButtonClick();
            }
        });

        password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER
                             || event.getKeyCode() == KeyEvent.FLAG_EDITOR_ACTION)
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onLoginButtonClick();
                    return true;
                }
                return false;
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof WelcomeView)) {
            throw new IllegalArgumentException("not an instance of WelcomeView");
        }
        handler = new LoginHandler((WelcomeView) activity);
    }

    public void onLoginButtonClick() {
        handler.login(userName.getText().toString(), password.getText().toString());
    }
}