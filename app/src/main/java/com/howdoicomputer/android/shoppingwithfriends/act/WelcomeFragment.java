package com.howdoicomputer.android.shoppingwithfriends.act;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.howdoicomputer.android.shoppingwithfriends.R;
import com.howdoicomputer.android.shoppingwithfriends.view.WelcomeView;

public class WelcomeFragment extends Fragment {
    private WelcomeAct container;
    private Button     toLoginButton;
    private Button     toRegisterButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);

        toLoginButton = (Button) rootView.findViewById(R.id.signIn_button);
        toRegisterButton = (Button) rootView.findViewById(R.id.register_button);

        toLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapFragmrent(new LoginFragment());
            }
        });

        toRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapFragmrent(new RegisterFragment());
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
        container = (WelcomeAct) activity;
    }

    /**
     * Replace current fragment with new fragment.
     *
     * @param newFrag new fragment to be shown
     */
    public void swapFragmrent(Fragment newFrag) {
        FragmentTransaction transaction = container.getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.welcomeFragmentContainer, newFrag);
        transaction.addToBackStack(null);    // let user navigate back to previous fragment

        transaction.commit();
    }

}