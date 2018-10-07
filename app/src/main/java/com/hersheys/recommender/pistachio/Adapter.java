package com.hersheys.recommender.pistachio;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
            ratingBar = itemView.findViewById(R.id.ratingBar2);
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
        Picasso.with(mContext).load(mData.get(position).getUri()).into(holder.background);
        holder.background.setClipToOutline(true);

        holder.background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent movieIntent = new Intent(mContext, Movie.class);
                mContext.startActivity(movieIntent);
            }
        });

        holder.ratingBar.setRating(mData.get(position).getInitialRating());

        String frag = mData.get(position).getFragment();
        if(frag.equals("myRatings")){
            holder.submitRatingButton.setText("Save");
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference MovieRef = database.getReference().child("Movies").child(Integer.toString(mData.get(position).getMovieId()));
        final Set globalSet = new HashSet<Integer>(50);
        final DatabaseReference UserRef = database.getReference("Users");
        final Random random = new Random();

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
                String frag = mData.get(position).getFragment();
                if(frag.equals("newRatings")){
                    if(position>=0){
                        globalSet.add(mData.get(position).getMovieId());
                        mData.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mData.size());
                        Toast.makeText(mContext, "Removed Card " + position, Toast.LENGTH_SHORT).show();
                    }

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        DatabaseReference userRatingRef = UserRef.child(user.getUid()).child("Ratings");
                        userRatingRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot ratings: dataSnapshot.getChildren()){
                                    int mid = Integer.parseInt(ratings.getKey());
                                    globalSet.add(mid);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else {
                        // No user is signed in
                    }

                    int newID;
                    do{
                        newID = random.nextInt(3883);
                    }while(globalSet.contains(newID));

                    mData.add(new item("https://firebasestorage.googleapis.com/v0/b/pistachio-8f641.appspot.com/o/images%2F"+Integer.toString(newID)+".jpg?alt=media&token=baff526a-ac90-4390-84ac-da4b9ee0f29a",newID,0,"newRatings"));
                    notifyItemInserted(mData.size()-1);
                }
                else if(frag.equals("myRatings")){
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(mContext, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(mContext);
                    }
                    builder.setTitle("Delete Rating?")
                            .setMessage("Your rating will be deleted permanently!")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference ref = database.getReference("Users");
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if (user != null) {
                                        // User is signed in
                                        DatabaseReference userRef = ref.child(user.getUid());
                                        DatabaseReference movieRef = userRef.child("Ratings");
                                        movieRef.child(Integer.toString(mData.get(position).getMovieId())).removeValue();
                                        Toast.makeText(mContext, "Rating Deleted!", Toast.LENGTH_LONG).show();
                                        if (position >= 0) {
                                            mData.remove(position);
                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(position, mData.size());
                                            //Toast.makeText(mContext, "Removed Card " + position, Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                    else{

                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        holder.submitRatingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                float rating = holder.ratingBar.getRating();
                if(rating!=0) {
                    String frag = mData.get(position).getFragment();

                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference("Users");
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            // User is signed in
                            DatabaseReference userRef = ref.child(user.getUid());
                            DatabaseReference movieRef = userRef.child("Ratings");
                            movieRef.child(Integer.toString(mData.get(position).getMovieId())).setValue(rating);
                            if(frag.equals("newRatings")) {
                                Toast.makeText(mContext, "Rating Submitted!", Toast.LENGTH_LONG).show();
                                if (position >= 0) {
                                    globalSet.add(mData.get(position).getMovieId());
                                    mData.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, mData.size());
                                    //Toast.makeText(mContext, "Removed Card " + position, Toast.LENGTH_SHORT).show();
                                    int newID;
                                    do{
                                        newID = random.nextInt(3883);
                                    }while(globalSet.contains(newID));

                                    mData.add(new item("https://firebasestorage.googleapis.com/v0/b/pistachio-8f641.appspot.com/o/images%2F"+Integer.toString(newID)+".jpg?alt=media&token=baff526a-ac90-4390-84ac-da4b9ee0f29a",newID,0,"newRatings"));
                                    notifyItemInserted(mData.size()-1);

                                }
                            }
                            else if(frag.equals("myRatings")){
                                Toast.makeText(mContext, "Rating Saved!", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            // No user is signed in}
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
