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
 * Created by Yoel Ivan on 2/8/2015.
 */
public class RegisterFragment extends Fragment {
    private LoginHandler         handler;
    private AutoCompleteTextView name;
    private AutoCompleteTextView userName;
    private AutoCompleteTextView email;
    private EditText             password;
    private EditText             passwordConfirm;
    private Button               register;
    private Button               cancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        name = (AutoCompleteTextView) rootView.findViewById(R.id.frag_reg_name_text);
        userName = (AutoCompleteTextView) rootView.findViewById(R.id.frag_reg_usrName_text);
        email = (AutoCompleteTextView) rootView.findViewById(R.id.frag_reg_email_text);
        password = (EditText) rootView.findViewById(R.id.frag_reg_pass1_text);
        passwordConfirm = (EditText) rootView.findViewById(R.id.frag_reg_pass2_text);
        register = (Button) rootView.findViewById(R.id.reg_button);
        cancel = (Button) rootView.findViewById(R.id.cancelRegister_button);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterButtonClick();
            }
        });

        passwordConfirm.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER
                             || event.getKeyCode() == KeyEvent.FLAG_EDITOR_ACTION)
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onRegisterButtonClick();
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

    private void onRegisterButtonClick() {
        handler.register(name.getText().toString(), userName.getText().toString(),
                email.getText().toString(), password.getText().toString(),
                passwordConfirm.getText().toString());
    }

}
