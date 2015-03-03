package com.howdoicomputer.android.shoppingwithfriends.view.act.friendlist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import com.google.gson.Gson;
import com.howdoicomputer.android.shoppingwithfriends.R;
import com.howdoicomputer.android.shoppingwithfriends.handler.FriendListHandler;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.Account;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.User;
import com.howdoicomputer.android.shoppingwithfriends.view.viewinterface.AppStateListener;
import com.howdoicomputer.android.shoppingwithfriends.view.viewinterface.FriendListView;
import com.howdoicomputer.android.shoppingwithfriends.view.viewinterface.ViewObjectUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link FriendListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendListFragment extends Fragment implements FriendListView {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CURRENTUSER_PARAM = "currentUSer";

    private RecyclerView         mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private User                 currentUser;
    private FriendListHandler    handler;
    private AppStateListener     mListener;

    private AlertDialog.Builder addFriendDialog;
    private AlertDialog         shownAddFriendDialog;
    private View                addFriendDialogView;
    private ViewObjectUtil      mUtil;

    public FriendListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param currentUser {@link User} of current user
     * @return A new instance of fragment FriendListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendListFragment newInstance(User currentUser) {
        FriendListFragment fragment = new FriendListFragment();
        Bundle args = new Bundle();
        args.putString(CURRENTUSER_PARAM, new Gson().toJson(currentUser));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.friend_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        mAdapter = new FriendListAdapter(currentUser.getFriendlist(), handler);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            currentUser = new Gson().fromJson(getArguments().getString(CURRENTUSER_PARAM),
                    User.class);
        }
        handler = new FriendListHandler(this, currentUser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_friend_list, container, false);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search_friend, menu);

        /*
        TODO:SEARCHVIEW WIDGET NOT WORKING WELP!!!

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(
                Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.searchFriend).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity()
        .getComponentName()));
        */
    }

    /* override this method to respond to the action bar */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //respond to add menu item
            case R.id.menu_item_addFriend:
                showAddFriendDialog();
            default:
                return super.onOptionsItemSelected(item);
        }
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

    private void showAddFriendDialog() {
        if (addFriendDialog == null) {
            addFriendDialogView = getLayoutInflater(null).inflate(R.layout.dialog_add_friend, null);
            final AutoCompleteTextView friendUserName = (AutoCompleteTextView) addFriendDialogView
                    .findViewById(R.id.add_friend_dialog_text);

            addFriendDialog = new AlertDialog.Builder(getActivity()).setTitle(
                    "Enter your friend username").setView(addFriendDialogView).setPositiveButton(
                    "Add", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            handler.add(friendUserName.getText().toString());
                            friendUserName.setText("");
                        }

                    }).setNegativeButton("Cancel", null).setOnKeyListener(
                    new DialogInterface.OnKeyListener() {

                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if ((keyCode == KeyEvent.KEYCODE_ENTER
                                         || keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER
                                         || event.getKeyCode() == KeyEvent.FLAG_EDITOR_ACTION)
                                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                                if (shownAddFriendDialog != null) {
                                    shownAddFriendDialog.dismiss();
                                }
                                handler.add(friendUserName.getText().toString());
                                friendUserName.setText("");
                                return true;
                            }
                            return false;
                        }

                    });
        } else if (addFriendDialogView != null && addFriendDialogView.getParent() != null) {
            ((ViewGroup) addFriendDialogView.getParent()).removeView(addFriendDialogView);
        }
        shownAddFriendDialog = addFriendDialog.show();
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
}
