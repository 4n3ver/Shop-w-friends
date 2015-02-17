package com.howdoicomputer.android.shoppingwithfriends.act;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.howdoicomputer.android.shoppingwithfriends.R;
import com.howdoicomputer.android.shoppingwithfriends.model.User;

import java.util.ArrayList;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendViewHolder> {
    private ArrayList<User> mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public FriendListAdapter(ArrayList<User> dataSet) {
        if (dataSet == null) {
            throw new IllegalArgumentException("data set is null");
        }
        mDataset = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FriendListAdapter.FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_friend_list,
                parent, false);


        // set the view's size, margins, paddings and layout parameters

        return new FriendViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        User friend = mDataset.get(position);
        holder.name.setText(String.valueOf(friend.getName()));
        holder.userName.setText(String.valueOf(friend.getUserName()));
        holder.rating.setText(String.valueOf(friend.getRating()));
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
        TextView name;
        TextView userName;
        TextView rating;

        public FriendViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.friend_name);
            userName = (TextView) itemView.findViewById(R.id.friend_userName);
            rating = (TextView) itemView.findViewById(R.id.friend_rating);
        }
    }
}