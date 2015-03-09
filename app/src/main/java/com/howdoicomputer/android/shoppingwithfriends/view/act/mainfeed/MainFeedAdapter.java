package com.howdoicomputer.android.shoppingwithfriends.view.act.mainfeed;

/**
 * Created by Yoel Ivan on 3/2/2015.
 */

import android.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.howdoicomputer.android.shoppingwithfriends.R;
import com.howdoicomputer.android.shoppingwithfriends.handler.MainFeedHandler;
import com.howdoicomputer.android.shoppingwithfriends.model.pojo.Item;

import java.util.ArrayList;

/**
 * {@link MainFeedAdapter} is a class to holds and manages data to be shown on
 * {@link MainFeedFragment}
 *
 * @author Yoel Ivan
 */
public class MainFeedAdapter extends RecyclerView.Adapter<MainFeedAdapter.MainFeedViewHolder> {
    private ArrayList<Item> mDataset;
    private MainFeedHandler mHandler;


    /**
     * Creates {@link MainFeedAdapter} object.
     *
     * @param handler presenter than handles user friend list
     */
    public MainFeedAdapter(MainFeedHandler handler) {
        mDataset = handler.getDataSet();
        mHandler = handler;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MainFeedAdapter.MainFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_layout, parent,
                false);

        // set the view's size, margins, paddings and layout parameters

        return new MainFeedViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MainFeedViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Item post = mDataset.get(position);
        holder.name.setText(String.valueOf(post.getPosterUserName()));
        holder.itemName.setText(String.valueOf(post.getItemName()));
        holder.price.setText(String.valueOf(post.getPrice()));
        //        holder.setOnClickListener(new View.OnClickListener() {
        //
        //            @Override
        //            public void onClick(View v) {
        //                holder.detailedView.name.setText(post.getName());
        //                holder.detailedView.userName.setText(post.getUserName());
        //                holder.detailedView.email.setText(post.getEmail());
        //                holder.detailedView.rating.setText("" + post.getRating());
        //                holder.detailedView.reportCount.setText("" + post.getSalesReported());
        //                holder.detailedView.showDetailedDialog();
        //            }
        //        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    } //TODO:CHANGe tHIS


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public final static class MainFeedViewHolder extends RecyclerView.ViewHolder {
        ImageView          posterImage;
        ImageView          itemImage;
        TextView           itemName;
        TextView           name;
        TextView           price;
        DetailedViewHolder detailedView;
        private Button clickSpace;

        /**
         * Create an object to hold the view that display friend's basic data.
         *
         * @param itemView reference to the layout's {@link View}
         */
        public MainFeedViewHolder(View itemView) {
            super(itemView);
            clickSpace = (Button) itemView.findViewById(R.id.feed_layout_clickspace);
            posterImage = (ImageView) itemView.findViewById(R.id.poster_image);
            itemImage = (ImageView) itemView.findViewById(R.id.poster_image);
            itemName = (TextView) itemView.findViewById(R.id.item_name);
            name = (TextView) itemView.findViewById(R.id.poster_name);
            price = (TextView) itemView.findViewById(R.id.posted_price);

            //            detailedView = new DetailedViewHolder(LayoutInflater.from(clickSpace
            // .getContext())
            //                    .inflate(R.layout.friend_detailed_layout, null));
        }

        /**
         * Attach a {@link View.OnClickListener} to the {@link MainFeedViewHolder}.
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
        private class DetailedViewHolder {
            TextView name;
            TextView userName;
            TextView email;
            TextView rating;
            TextView reportCount;
            Button   removeButton;
            private View                layout;
            private AlertDialog.Builder detailedDialog;
            private AlertDialog         shownDialog;

            /**
             * Create an object to hold the detailed view that display friend's detailed data.
             *
             * @param detailedView reference to the layout's {@link View}
             */
            private DetailedViewHolder(View detailedView) {
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
