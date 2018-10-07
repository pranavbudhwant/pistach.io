package com.hersheys.recommender.pistachio.MovieAdapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.hersheys.recommender.pistachio.Models.MovieTitleChild;
import com.hersheys.recommender.pistachio.Models.MovieTitleParent;
import com.hersheys.recommender.pistachio.R;
import com.hersheys.recommender.pistachio.ViewHolders.MovieTitleChildViewHolder;
import com.hersheys.recommender.pistachio.ViewHolders.MovieTitleParentViewHolder;

import java.util.List;

public class MovieAdapter extends ExpandableRecyclerAdapter<MovieTitleParentViewHolder, MovieTitleChildViewHolder> {

    LayoutInflater inflater;

    public MovieAdapter(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        inflater = LayoutInflater.from(context);

    }

    @Override
    public MovieTitleParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.movie_details_card_parent, viewGroup, false);
        return  new MovieTitleParentViewHolder(view);
    }

    @Override
    public MovieTitleChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.movie_details_card_child, viewGroup, false);
        return  new MovieTitleChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(MovieTitleParentViewHolder movieTitleParentViewHolder, int i, Object o) {
        MovieTitleParent title = (MovieTitleParent)o;
        movieTitleParentViewHolder._textView.setText(title.getTitle());
    }

    @Override
    public void onBindChildViewHolder(MovieTitleChildViewHolder movieTitleChildViewHolder, int i, Object o) {
        MovieTitleChild title = (MovieTitleChild)o;
        movieTitleChildViewHolder.childText.setText(title.getChildText());

    }
}
