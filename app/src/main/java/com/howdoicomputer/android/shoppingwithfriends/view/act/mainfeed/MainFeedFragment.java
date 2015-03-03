package com.howdoicomputer.android.shoppingwithfriends.view.act.mainfeed;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.howdoicomputer.android.shoppingwithfriends.R;
import com.howdoicomputer.android.shoppingwithfriends.handler.MainFeedHandler;
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

    private RecyclerView         mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private User                 currentUser;
    private AppStateListener     mListener;
    private ViewObjectUtil       mUtil;
    private MainFeedHandler      handler;
    private FloatingActionButton addInterestButton;

    private AlertDialog.Builder addInterestItemDialog;
    private AlertDialog         shownAddInterestItemDialog;
    private View                addItemInterestDialogView;

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
        //handler = new MainFeedHandler(this, currentUser);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.mainfeed_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        //mAdapter = new MainFeedAdapter(handler);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main_feed, container, false);

        addInterestButton = (FloatingActionButton) rootView.findViewById(R.id.add_post_interest);
        addInterestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddItemInterestDialog();
            }
        });


        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AppStateListener) activity;
            mUtil = (ViewObjectUtil) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() + " must implement AppStateListener & ViewObjectUtil");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mUtil = null;
    }

    @Override
    public AppStateListener getAppStateListener() {
        return mListener;
    }

    @Override
    public ViewObjectUtil getUiUtil() {
        return mUtil;
    }

    @Override
    public void refreshView() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateAccount(Account acc) {
        currentUser = (User) acc;
    }

    private void showAddItemInterestDialog() {
        if (addInterestItemDialog == null) {
            addItemInterestDialogView = getLayoutInflater(null).inflate(
                    R.layout.dialog_add_interest_item, null);
            final AutoCompleteTextView itemName = (AutoCompleteTextView) addItemInterestDialogView
                    .findViewById(R.id.interest_item_name);

            addInterestItemDialog = new AlertDialog.Builder(getActivity()).setTitle(
                    "Post item you want...").setView(addItemInterestDialogView).setOnKeyListener(
                    new DialogInterface.OnKeyListener() {

                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if ((keyCode == KeyEvent.KEYCODE_ENTER
                                         || keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER
                                         || event.getKeyCode() == KeyEvent.FLAG_EDITOR_ACTION)
                                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                                if (shownAddInterestItemDialog != null) {
                                    shownAddInterestItemDialog.dismiss();
                                }
                                //TODO:handler.add(itemName.getText().toString());
                                itemName.setText("");
                                return true;
                            }
                            return false;
                        }

                    });
        } else if (addItemInterestDialogView != null
                && addItemInterestDialogView.getParent() != null) {
            ((ViewGroup) addItemInterestDialogView.getParent()).removeView(
                    addItemInterestDialogView);
        }
        shownAddInterestItemDialog = addInterestItemDialog.show();
    }
}
