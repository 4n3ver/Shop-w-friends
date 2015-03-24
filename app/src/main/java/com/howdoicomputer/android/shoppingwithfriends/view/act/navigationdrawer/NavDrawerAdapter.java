package com.howdoicomputer.android.shoppingwithfriends.view.act.navigationdrawer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.howdoicomputer.android.shoppingwithfriends.R;
import com.howdoicomputer.android.shoppingwithfriends.view.act.friendlist.FriendListAdapter;
import com.howdoicomputer.android.shoppingwithfriends.view.act.friendlist.FriendListFragment;

import java.util.ArrayList;

/**
 * {@link FriendListAdapter} is a class to holds and manages data to be shown on
 * {@link FriendListFragment}
 *
 * @author Yoel Ivan
 * @version 1.5
 */
public class NavDrawerAdapter extends RecyclerView.Adapter<NavDrawerAdapter.NavMenuHolder> {
    private ArrayList<MenuResourceHolder> mDataset;

    /**
     * Creates {@link NavDrawerAdapter} object.
     *
     * @param dataSet presenter than handles user friend list
     */
    public NavDrawerAdapter(ArrayList<MenuResourceHolder> dataSet) {
        mDataset = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NavDrawerAdapter.NavMenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_drawer_menu_layout,
                parent, false);

        // set the view's size, margins, paddings and layout parameters

        return new NavDrawerAdapter.NavMenuHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final NavMenuHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.icon.setImageResource(mDataset.get(position).mIconResID);
        holder.text.setText(mDataset.get(position).mText);
        holder.setOnClickListener(mDataset.get(position).mClickListener);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public final static class NavMenuHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView  text;
        private Button clickSpace;

        /**
         * Create an object to hold the view that display friend's basic data.
         *
         * @param menuView reference to the layout's {@link View}
         */
        public NavMenuHolder(View menuView) {
            super(menuView);
            clickSpace = (Button) menuView.findViewById(R.id.drawer_menu_clickspace);
            icon = (ImageView) menuView.findViewById(R.id.drawer_menu_icon);
            text = (TextView) menuView.findViewById(R.id.drawer_menu_text);
        }

        /**
         * Attach a {@link View.OnClickListener} to the {@link NavMenuHolder}.
         *
         * @param clickListener {@link View.OnClickListener} object to be attached
         */
        public void setOnClickListener(View.OnClickListener clickListener) {
            clickSpace.setOnClickListener(clickListener);
        }
    }

    public static class MenuResourceHolder {
        int                  mIconResID;
        String               mText;
        View.OnClickListener mClickListener;

        public MenuResourceHolder(int iconResourceID, String text, View.OnClickListener listener) {
            mIconResID = iconResourceID;
            mText = text;
            mClickListener = listener;
        }
    }
}