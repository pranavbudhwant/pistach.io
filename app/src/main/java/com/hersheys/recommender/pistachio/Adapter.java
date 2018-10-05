package com.hersheys.recommender.pistachio;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.*;

public class Adapter extends RecyclerView.Adapter<Adapter.myViewHolder>{

    Context mContext;
    List<item> mData;

    public Adapter(Context mContext, List<item> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        ImageView background;
        TextView title;
        TextView genres;
        TextView imdb;
        ImageButton crossButton;
        public myViewHolder(View itemView) {
            super(itemView);
            background = itemView.findViewById(R.id.CardBackground);
            title = itemView.findViewById(R.id.Title);
            genres = itemView.findViewById(R.id.genre);
            imdb = itemView.findViewById(R.id.imdb);
            crossButton = itemView.findViewById(R.id.crossButton);
        }
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.card_item,parent,false);

        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, final int position) {
        //holder.background.setImageResource(mData.get(position).getBackground());
        Picasso.with(mContext).load(mData.get(position).getUri()).into(holder.background);
        holder.title.setText(mData.get(position).getTitle());
        holder.genres.setText(mData.get(position).getGenres());
        holder.imdb.setText(mData.get(position).getRating());
        holder.background.setClipToOutline(true);
        holder.crossButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(position>=0) {
                    mData.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mData.size());
                    Toast.makeText(mContext, "Removed Card " + position, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return mData.size();
    }


}
