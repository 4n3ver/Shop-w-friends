package com.howdoicomputer.android.shoppingwithfriends.view.act.mainfeed;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.howdoicomputer.android.shoppingwithfriends.R;
import com.howdoicomputer.android.shoppingwithfriends.handler.MainFeedHandler;
import com.howdoicomputer.android.shoppingwithfriends.view.viewinterface.AppStateListener;
import com.howdoicomputer.android.shoppingwithfriends.view.viewinterface.MainFeedView;
import com.howdoicomputer.android.shoppingwithfriends.view.viewinterface.ViewObjectUtil;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;

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
    private AppStateListener     mListener;
    private ViewObjectUtil       mUtil;
    private MainFeedHandler      handler;
    private FloatingActionsMenu addPost;
    private FloatingActionButton addInterestButton;
    private FloatingActionButton addReportButton;

    private AlertDialog.Builder addInterestItemDialog;
    private AlertDialog         shownAddInterestItemDialog;
    private View                addItemInterestDialogView;

    private AlertDialog.Builder addReportItemDialog;
    private AlertDialog         shownAddReportItemDialog;
    private View                addItemReportDialogView;

    private SwipeRefreshLayout mSwipeToRefresh;
    private LinearLayoutManager mLinearLayoutManager;

    public MainFeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainFeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFeedFragment newInstance() {
        MainFeedFragment fragment = new MainFeedFragment();
        //        Bundle args = new Bundle();
        //        args.putString(CURRENTUSER_PARAM, new Gson().toJson(currentUser));
        //        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //            currentUser = new Gson().fromJson(getArguments().getString
            // (CURRENTUSER_PARAM),
            //                    User.class);
        }
        handler = new MainFeedHandler(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.mainfeed_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        // specify an adapter
        mAdapter = new MainFeedAdapter(handler);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int mLastFirstVisibleItem = 0;
            boolean nextUp = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final int currentFirstVisibleItem = mLinearLayoutManager
                        .findFirstVisibleItemPosition();

                if (currentFirstVisibleItem > this.mLastFirstVisibleItem && !nextUp) {
                    addPost.animate().cancel();
                    addPost.animate().translationYBy(350);
                    nextUp = true;
                } else if (currentFirstVisibleItem < this.mLastFirstVisibleItem && nextUp) {
                    addPost.animate().cancel();
                    addPost.animate().translationYBy(-350);
                    nextUp = false;
                }

                this.mLastFirstVisibleItem = currentFirstVisibleItem;
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main_feed, container, false);

        addPost = (FloatingActionsMenu) rootView.findViewById(R.id.add_post);

        addInterestButton = (FloatingActionButton) rootView.findViewById(R.id.add_post_interest);
        addInterestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddItemInterestDialog();
            }
        });

        addReportButton = (FloatingActionButton) rootView.findViewById(R.id.add_post_report);
        addReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddItemReportDialog();
            }
        });

        mSwipeToRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_feed);
        mSwipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }

            void refreshItems() {
                handler.fetchFeed();
                onItemsLoadComplete();
            }

            void onItemsLoadComplete() {
                // Update the adapter and notify data set changed
                // ...

                // Stop refresh animation
                mSwipeToRefresh.setRefreshing(false);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.fetchFeed();
            }
        }, 250);

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


    private void showAddItemInterestDialog() {
        if (addInterestItemDialog == null) {
            createAddItemInterestDialog();
        } else if (addItemInterestDialogView != null
                && addItemInterestDialogView.getParent() != null) {
            ((ViewGroup) addItemInterestDialogView.getParent()).removeView(
                    addItemInterestDialogView);
        }
        shownAddInterestItemDialog = addInterestItemDialog.show();
    }

    private void createAddItemInterestDialog() {
        addItemInterestDialogView = getLayoutInflater(null).inflate(
                R.layout.dialog_add_interest_item, null);
        final AutoCompleteTextView itemName = (AutoCompleteTextView) addItemInterestDialogView
                .findViewById(R.id.interest_item_name);
        final EditText itemPrice = (EditText) addItemInterestDialogView.findViewById(
                R.id.interest_item_price);
        ImageButton submit = (ImageButton) addItemInterestDialogView.findViewById(
                R.id.interest_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitInterestItem(itemName, itemPrice);
            }
        });

        addInterestItemDialog = new AlertDialog.Builder(getActivity()).setTitle("Report an item...")
                .setView(addItemInterestDialogView).setOnKeyListener(
                        new DialogInterface.OnKeyListener() {

                            @Override
                            public boolean onKey(DialogInterface dialog, int keyCode,
                                    KeyEvent event) {
                                if ((keyCode == KeyEvent.KEYCODE_ENTER
                                             || keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER
                                             || event.getKeyCode() == KeyEvent.FLAG_EDITOR_ACTION)
                                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                                    submitInterestItem(itemName, itemPrice);
                                    return true;
                                }
                                return false;
                            }
                        });
    }

    private void submitInterestItem(AutoCompleteTextView itemName, EditText itemPrice) {
        if (shownAddInterestItemDialog != null) {
            shownAddInterestItemDialog.dismiss();
        }
        handler.postItemOfInterest(itemName.getText().toString(),
                mListener.getLatestAccount().getUserName(), itemPrice.getText().toString());
        itemName.setText("");
        itemPrice.setText("");
    }

    private void showAddItemReportDialog() {
        if (addReportItemDialog == null) {
            createAddItemReportDialog();
        } else if (addItemReportDialogView != null && addItemReportDialogView.getParent() != null) {
            ((ViewGroup) addItemReportDialogView.getParent()).removeView(addItemReportDialogView);
        }
        shownAddReportItemDialog = addReportItemDialog.show();
    }

    private void createAddItemReportDialog() {
        addItemReportDialogView = getLayoutInflater(null).inflate(R.layout.dialog_report_sale,
                null);
        final AutoCompleteTextView itemName = (AutoCompleteTextView) addItemReportDialogView
                .findViewById(R.id.report_item_name);
        final EditText itemPrice = (EditText) addItemReportDialogView.findViewById(
                R.id.report_item_price);
        MultiStateToggleButton button;
        ImageButton submit = (ImageButton) addItemReportDialogView.findViewById(R.id.report_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReportItem(itemName, itemPrice);
            }
        });

        addReportItemDialog = new AlertDialog.Builder(getActivity()).setTitle("Report an item...")
                .setView(addItemReportDialogView).setOnKeyListener(
                        new DialogInterface.OnKeyListener() {

                            @Override
                            public boolean onKey(DialogInterface dialog, int keyCode,
                                    KeyEvent event) {
                                if ((keyCode == KeyEvent.KEYCODE_ENTER
                                             || keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER
                                             || event.getKeyCode() == KeyEvent.FLAG_EDITOR_ACTION)
                                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                                    submitReportItem(itemName, itemPrice);
                                    return true;
                                }
                                return false;
                            }
                        });
    }

    private void submitReportItem(AutoCompleteTextView itemName, EditText itemPrice) {
        if (shownAddReportItemDialog != null) {
            shownAddReportItemDialog.dismiss();
        }
        handler.postItemOfReport(itemName.getText().toString(),
                mListener.getLatestAccount().getUserName(), itemPrice.getText().toString());
        itemName.setText("");
        itemPrice.setText("");
    }
}
