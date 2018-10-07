package com.hersheys.recommender.pistachio.ViewHolders;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.hersheys.recommender.pistachio.R;

public class MovieTitleChildViewHolder extends ChildViewHolder {
    public TextView childText;

    public MovieTitleChildViewHolder(View itemView) {
        super(itemView);
        childText = (TextView)itemView.findViewById(R.id.childText);
    }
}
