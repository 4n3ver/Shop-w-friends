package com.howdoicomputer.android.shoppingwithfriends.view.act;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.howdoicomputer.android.shoppingwithfriends.R;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.Account;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.User;
import com.howdoicomputer.android.shoppingwithfriends.view.viewinterface.AppStateListener;
import com.howdoicomputer.android.shoppingwithfriends.view.viewinterface.MainFeedView;
import com.howdoicomputer.android.shoppingwithfriends.view.viewinterface.ViewObjectUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link MainFeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFeedFragment extends Fragment implements MainFeedView {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CURRENTUSER_PARAM = "currentUser";

    private User currentUser;

    private AppStateListener mListener;

    public MainFeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param currentUser {@link User} of current user
     * @return A new instance of fragment MainFeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFeedFragment newInstance(User currentUser) {
        MainFeedFragment fragment = new MainFeedFragment();
        Bundle args = new Bundle();
        args.putString(CURRENTUSER_PARAM, new Gson().toJson(currentUser));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentUser = new Gson().fromJson(getArguments().getString(CURRENTUSER_PARAM),
                    User.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main_feed, container, false);


        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AppStateListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement AppStateListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public AppStateListener getAppStateListener() {
        return null;
    }

    @Override
    public ViewObjectUtil getUiUtil() {
        return null;
    }

    @Override
    public void refreshView() {

    }

    @Override
    public void updateAccount(Account acc) {
        currentUser = (User) acc;
    }
}
