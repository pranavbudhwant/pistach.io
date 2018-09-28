package com.hersheys.recommender.pistachio;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.*;

public class Adapter extends RecyclerView.Adapter<Adapter.myViewHolder>{

    Context mContext;
    List<item> mData;

    public Adapter(Context mContext, List<item> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.card_item,parent,false);

        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        holder.background.setImageResource(mData.get(position).getBackground());
        holder.title.setText(mData.get(position).getTitle());
        holder.genres.setText(mData.get(position).getGenres());
        holder.imdb.setText(mData.get(position).getRating());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        ImageView background;
        TextView title;
        TextView genres;
        TextView imdb;
        public myViewHolder(View itemView) {
            super(itemView);
            background = itemView.findViewById(R.id.CardBackground);
            title = itemView.findViewById(R.id.Title);
            genres = itemView.findViewById(R.id.genre);
            imdb = itemView.findViewById(R.id.imdb);
        }
    }
}
