package com.howdoicomputer.android.shoppingwithfriends.act;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.howdoicomputer.android.shoppingwithfriends.R;

/**
 * Created by Yoel Ivan on 2/7/2015.
 */
public class LoginFragmentExp extends Fragment {
    private Activity welcomeAct;
    private AutoCompleteTextView userName;
    private EditText password;
    private Button login;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater
                .inflate(com.howdoicomputer.android.shoppingwithfriends.R.layout.fragment_login,
                         container, false);
        welcomeAct = getActivity();
        userName = (AutoCompleteTextView) welcomeAct.findViewById(R.id.frag_login_usrName_text);
        password = (EditText) welcomeAct.findViewById(R.id.frag_login_password_text);
        login = (Button) welcomeAct.findViewById(R.id.login_button);
        return rootView;
    }

}
