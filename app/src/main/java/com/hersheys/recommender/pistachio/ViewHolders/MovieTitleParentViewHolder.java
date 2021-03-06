package com.hersheys.recommender.pistachio.ViewHolders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.hersheys.recommender.pistachio.R;

public class MovieTitleParentViewHolder extends ParentViewHolder {
    public TextView _textView;
    public ImageButton _imageButton;
    public MovieTitleParentViewHolder(View itemView) {
        super(itemView);
        _textView = (TextView)itemView.findViewById(R.id.parentTitle);
        _imageButton = (ImageButton)itemView.findViewById(R.id.expandArrow);
    }
}
