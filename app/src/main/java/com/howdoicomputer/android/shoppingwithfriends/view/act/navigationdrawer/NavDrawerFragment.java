package com.howdoicomputer.android.shoppingwithfriends.view.act.navigationdrawer;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.howdoicomputer.android.shoppingwithfriends.R;
import com.howdoicomputer.android.shoppingwithfriends.handler.NavigationHandler;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.User;
import com.howdoicomputer.android.shoppingwithfriends.view.act.friendlist.FriendListFragment;
import com.howdoicomputer.android.shoppingwithfriends.view.act.mainfeed.MainFeedFragment;
import com.howdoicomputer.android.shoppingwithfriends.view.viewinterface.AppStateListener;
import com.howdoicomputer.android.shoppingwithfriends.view.viewinterface.NavDrawerView;
import com.howdoicomputer.android.shoppingwithfriends.view.viewinterface.ViewObjectUtil;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavDrawerFragment extends Fragment implements NavDrawerView {
    private static final String PREF_FILE_NAME              = "navDrawerAware";
    private static final String KEY_USER_IS_AWARE_OF_DRAWER = "key_navDrawerAware";
    private TextView              mName;
    private RecyclerView          mRecyclerView;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout          drawerLayout;
    private boolean               userIsAwareOfDrawer;
    private boolean               userIsAwareOfDrawerSavedInstance;
    private View                  containerView;
    private NavigationHandler     handler;
    private AppStateListener      mListener;
    private FragmentLocation mCurrentShownView;
    private FragmentLocation mLastShownView;
    private MainFeedFragment      mff;
    private FriendListFragment    flf;

    public NavDrawerFragment() {
        // Required empty public constructor
    }

    public static void saveToPreferences(Context context, String preferenceName,
            String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName,
            String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.drawer_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        mRecyclerView.setAdapter(new NavDrawerAdapter(populateNavDrawerMenu()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userIsAwareOfDrawerSavedInstance = Boolean.valueOf(readFromPreferences(getActivity(),
                KEY_USER_IS_AWARE_OF_DRAWER, "false"));

        if (savedInstanceState != null) {
            userIsAwareOfDrawerSavedInstance = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nav_drawer, container, false);
    }

    public void setUp(int fragmentID, DrawerLayout drawerLayout, final Toolbar actBar) {
        mff = MainFeedFragment.newInstance();
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.mainFragmentContainer,
                mff).commit();
        mCurrentShownView = FragmentLocation.MAIN_FEED;

        this.handler = new NavigationHandler();
        this.containerView = getActivity().findViewById(fragmentID);
        this.drawerLayout = drawerLayout;
        this.drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, actBar,
                R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (mName == null) {
                    mName = ((TextView) getActivity().findViewById(R.id.helloName));
                }
                mName.setText("Hello, " + ((User) mListener.getLatestAccount()).getName());
                actBar.setAlpha(1 - slideOffset / 2);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!userIsAwareOfDrawer) {
                    userIsAwareOfDrawer = true;
                    saveToPreferences(getActivity(), KEY_USER_IS_AWARE_OF_DRAWER,
                            "" + userIsAwareOfDrawer);
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }


        };

        if (!userIsAwareOfDrawer && !userIsAwareOfDrawerSavedInstance) {
            drawerLayout.openDrawer(containerView);
        }
        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.syncState();
            }
        });
    }

    private ArrayList<NavDrawerAdapter.MenuResourceHolder> populateNavDrawerMenu() {
        ArrayList<NavDrawerAdapter.MenuResourceHolder> menuData = new ArrayList<>(3);

        menuData.add(new NavDrawerAdapter.MenuResourceHolder(R.drawable.ic_basket, "Shop",
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (mCurrentShownView != FragmentLocation.MAIN_FEED) {
                            mLastShownView = null;
                            mCurrentShownView = FragmentLocation.MAIN_FEED;
                            drawerLayout.closeDrawers();
                            new Thread(new Runnable() {
                                public void run() {
                                    FragmentTransaction transaction = getActivity()
                                            .getSupportFragmentManager().beginTransaction();
                                    if (mff == null) {
                                        mff = MainFeedFragment.newInstance();
                                    }
                                    transaction.replace(R.id.mainFragmentContainer, mff);
                                    getActivity().getSupportFragmentManager().popBackStack();
                                    transaction.commit();
                                }
                            }).start();
                        }
                    }
                }));

        menuData.add(new NavDrawerAdapter.MenuResourceHolder(R.drawable.ic_account_multiple,
                "Friend", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCurrentShownView != FragmentLocation.FRIEND_LIST) {
                    mLastShownView = mCurrentShownView;
                    mCurrentShownView = FragmentLocation.FRIEND_LIST;
                    drawerLayout.closeDrawers();
                    new Thread(new Runnable() {
                        public void run() {
                            FragmentTransaction transaction = getActivity()
                                    .getSupportFragmentManager().beginTransaction();
                            if (flf == null) {
                                flf = FriendListFragment.newInstance();
                            }
                            transaction.replace(R.id.mainFragmentContainer, flf);
                            getActivity().getSupportFragmentManager().popBackStack();
                            transaction.addToBackStack(
                                    null);    // let user navigate back to previous fragment
                            transaction.commit();
                        }
                    }).start();
                }
            }
        }));

        menuData.add(new NavDrawerAdapter.MenuResourceHolder(R.drawable.ic_logout, "Logout",
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        drawerLayout.closeDrawers();
                        mListener.onLoggedOut();
                        handler.logout();
                    }
                }));

        return menuData;
    }

    public void onBackPressed() {
        FragmentLocation temp = mLastShownView;
        mLastShownView = mCurrentShownView;
        mCurrentShownView = temp;
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public AppStateListener getAppStateListener() {
        return mListener;
    }

    @Override
    public ViewObjectUtil getUiUtil() {
        return null;
    }

    @Override
    public void refreshView() {

    }

    private enum FragmentLocation {
        MAIN_FEED, FRIEND_LIST
    }
}