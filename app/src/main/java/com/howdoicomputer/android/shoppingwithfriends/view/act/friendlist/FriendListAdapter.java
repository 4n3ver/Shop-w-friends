package com.howdoicomputer.android.shoppingwithfriends.view.act.friendlist;

import android.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.howdoicomputer.android.shoppingwithfriends.R;
import com.howdoicomputer.android.shoppingwithfriends.handler.FriendListHandler;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.User;

import java.util.List;

/**
 * {@link FriendListAdapter} is a class to holds and manages data to be shown on
 * {@link FriendListFragment}
 *
 * @author Yoel Ivan
 * @version 1.5
 */
public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendViewHolder> {
    private List<User>        mDataset;
    private FriendListHandler mHandler;

    /**
     * Creates {@link FriendListAdapter} object.
     *
     * @param handler presenter than handles user friend list
     */
    public FriendListAdapter(FriendListHandler handler) {
        mDataset = handler.getDataSet();
        mHandler = handler;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FriendListAdapter.FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_layout, parent,
                false);

        // set the view's size, margins, paddings and layout parameters

        return new FriendViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final FriendViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final User friend = mDataset.get(position);
        holder.name.setText(String.valueOf(friend.getName()));
        holder.userName.setText(String.valueOf(friend.getUserName()));
        holder.rating.setText(String.valueOf(friend.getRating()));
        holder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (holder.detailedView == null) {
                    holder.detailedView = new FriendViewHolder.DetailedViewHolder(
                            LayoutInflater.from(holder.clickSpace.getContext()).inflate(
                                    R.layout.friend_detailed_layout, null));
                }
                holder.detailedView.name.setText(String.valueOf(friend.getName()));
                holder.detailedView.userName.setText(String.valueOf(friend.getUserName()));
                holder.detailedView.email.setText(String.valueOf(friend.getEmail()));
                holder.detailedView.rating.setText(String.valueOf(friend.getRating()));
                holder.detailedView.reportCount.setText(String.valueOf(friend.getSalesReported()));
                holder.detailedView.removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mHandler.remove(friend);
                        holder.detailedView.dismissDetailedDialog();
                    }
                });
                holder.detailedView.showDetailedDialog();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public final static class FriendViewHolder extends RecyclerView.ViewHolder {
        final         TextView name;
        final         TextView userName;
        final         TextView rating;
        private final Button   clickSpace;
        DetailedViewHolder detailedView;

        /**
         * Create an object to hold the view that display friend's basic data.
         *
         * @param itemView reference to the layout's {@link View}
         */
        public FriendViewHolder(View itemView) {
            super(itemView);
            clickSpace = (Button) itemView.findViewById(R.id.friend_layout_clickspace);
            name = (TextView) itemView.findViewById(R.id.friend_name);
            userName = (TextView) itemView.findViewById(R.id.friend_userName);
            rating = (TextView) itemView.findViewById(R.id.friend_rating);
        }

        /**
         * Attach a {@link View.OnClickListener} to the {@link FriendViewHolder}.
         *
         * @param clickListener {@link View.OnClickListener} object to be attached
         */
        public void setOnClickListener(View.OnClickListener clickListener) {
            clickSpace.setOnClickListener(clickListener);
        }

        /**
         * {@link DetailedViewHolder} is an object to hold detailed view that display friend's
         * detailed data.
         */
        public static class DetailedViewHolder {
            final         TextView            name;
            final         TextView            userName;
            final         TextView            email;
            final         TextView            rating;
            final         TextView            reportCount;
            final         Button              removeButton;
            private final View                layout;
            private final AlertDialog.Builder detailedDialog;
            private       AlertDialog         shownDialog;

            /**
             * Create an object to hold the detailed view that display friend's detailed data.
             *
             * @param detailedView reference to the layout's {@link View}
             */
            public DetailedViewHolder(final View detailedView) {
                layout = detailedView;
                name = (TextView) detailedView.findViewById(R.id.friend_detailed_name);
                userName = (TextView) detailedView.findViewById(R.id.friend_detailed_user_name);
                email = (TextView) detailedView.findViewById(R.id.friend_detailed_email);
                rating = (TextView) detailedView.findViewById(R.id.friend_detailed_rating);
                reportCount = (TextView) detailedView.findViewById(
                        R.id.friend_detailed_report_count);
                removeButton = (Button) detailedView.findViewById(R.id.temporary_remove);
                detailedDialog = new AlertDialog.Builder(detailedView.getContext()).setView(
                        detailedView);
            }

            /**
             * Show the dialog that holds the layout of the detailed view.
             */
            public void showDetailedDialog() {
                if (layout.getParent() != null) {
                    ((ViewGroup) layout.getParent()).removeView(layout);
                }
                shownDialog = detailedDialog.show();
            }

            /**
             * Hide showed detailed dialog if any.
             */
            public void dismissDetailedDialog() {
                if (shownDialog != null) {
                    shownDialog.dismiss();
                }
            }
        }
    }
}