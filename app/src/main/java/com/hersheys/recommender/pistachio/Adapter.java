package com.hersheys.recommender.pistachio;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
        Button submitRatingButton;
        RatingBar ratingBar;
        public myViewHolder(View itemView) {
            super(itemView);
            background = itemView.findViewById(R.id.CardBackground);
            title = itemView.findViewById(R.id.Title);
            genres = itemView.findViewById(R.id.genre);
            imdb = itemView.findViewById(R.id.imdb);
            crossButton = itemView.findViewById(R.id.crossButton);
            submitRatingButton = itemView.findViewById(R.id.submitRatingButton);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.card_item,parent,false);

        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final myViewHolder holder, final int position) {
        //holder.background.setImageResource(mData.get(position).getBackground());
        Picasso.with(mContext).load(mData.get(position).getUri()).into(holder.background);
        //holder.title.setText(mData.get(position).getTitle());
        //holder.genres.setText(mData.get(position).getGenres());
        //holder.imdb.setText(mData.get(position).getRating());
        holder.background.setClipToOutline(true);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference MovieRef = database.getReference().child("Movies").child(Integer.toString(mData.get(position).getMovieId()));

        MovieRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title, genres, imdb;
                title = (String)dataSnapshot.child("title").getValue();
                genres = (String)dataSnapshot.child("genre").getValue();
                imdb = (String)dataSnapshot.child("imdb_rating").getValue();
                if(title!=null)
                    holder.title.setText(title);
                else
                    holder.title.setText("");

                if(genres!=null)
                    holder.genres.setText(genres);
                else
                    holder.genres.setText("Other");

                if(imdb!=null)
                    holder.imdb.setText("IMDB: " + imdb);
                else
                    holder.imdb.setText("IMDB: N/A");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

        holder.submitRatingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                float rating = holder.ratingBar.getRating();
                if(rating!=0) {
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference("Users");
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        DatabaseReference userRef = ref.child(user.getUid());
                        DatabaseReference movieRef = userRef.child("Ratings");
                        movieRef.child(Integer.toString(mData.get(position).getMovieId())).setValue(rating);
                        Toast.makeText(mContext, "Rating Submitted!",Toast.LENGTH_LONG).show();

                        if(position>=0) {
                            mData.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, mData.size());
                            //Toast.makeText(mContext, "Removed Card " + position, Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        // No user is signed in
                    }
                }
                else{
                    Toast.makeText(mContext, "Rating Cannot be Zero!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


}
