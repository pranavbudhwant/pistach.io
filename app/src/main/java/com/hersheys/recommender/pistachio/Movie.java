package com.hersheys.recommender.pistachio;

import android.content.Context;
import android.content.Intent;
import android.net.http.SslCertificate;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hersheys.recommender.pistachio.Models.MovieTitleChild;
import com.hersheys.recommender.pistachio.Models.MovieTitleParent;
import com.hersheys.recommender.pistachio.Models.TitleCreator;
import com.hersheys.recommender.pistachio.MovieAdapter.MovieAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Movie extends AppCompatActivity {
    int movieID;
    TextView title, genre, imdb;
    ImageView movieImage, bookmark;
    RatingBar ratingBar;

    RecyclerView recyclerView;

    public String movie_title, movie_genre, imdb_rating, movie_actors, movie_director, movie_country, movie_language, movie_duration, movie_awards, movie_plot;

    public Context mContext;

    public List<ParentObject> parentObject;

    Boolean bookmark_flag;

    Button submitButton;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((MovieAdapter)recyclerView.getAdapter()).onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        findViewById(R.id.crossButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        mContext = this;
        parentObject = new ArrayList<>();
        movieID = getIntent().getIntExtra("movieID",-1);

        recyclerView = (RecyclerView)findViewById(R.id.movieDetailsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        submitButton = findViewById(R.id.movie_submitRatingButton);
        ratingBar = findViewById(R.id.movie_ratingBar);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = ratingBar.getRating();
                if(rating!=0) {
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference("Users");
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        DatabaseReference userRef = ref.child(user.getUid());
                        DatabaseReference movieRef = userRef.child("Ratings");
                        movieRef.child(Integer.toString(movieID)).setValue(rating);
                        Toast.makeText(mContext, "Rating Saved!", Toast.LENGTH_LONG).show();
                    } else {
                        // No user is signed in}
                    }
                }
                else{
                    Toast.makeText(mContext, "Rating Cannot be Zero!",Toast.LENGTH_LONG).show();
                }

            }
        });


        bookmark = (ImageView)findViewById(R.id.bookmark);
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bookmark_flag){
                    //Movie is bookmarked - Remove the bookmark.
                    bookmark_flag = false;
                    bookmark.setImageResource(R.drawable.bookmark_fill);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference userBookmarkRef = database.getReference().child("Users").child(user.getUid()).child("Bookmarks");
                        userBookmarkRef.child(Integer.toString(movieID)).removeValue();
                    }
                    else{

                    }
                }
                else{
                    //Movie is not bookmarked - Add the movie to bookmarks.
                    bookmark_flag = true;
                    bookmark.setImageResource(R.drawable.bookmark_border);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference userBookmarkRef = database.getReference().child("Users").child(user.getUid()).child("Bookmarks");
                        userBookmarkRef.child(Integer.toString(movieID)).setValue(true);
                    }
                    else{

                    }
                }
            }
        });

        title = (TextView)findViewById(R.id.movie_title);
        genre = (TextView)findViewById(R.id.movie_genre);
        imdb = (TextView)findViewById(R.id.movie_imdb);
        movieImage = (ImageView)findViewById(R.id.imageView);
        ratingBar = (RatingBar)findViewById(R.id.movie_ratingBar);

        if(movieID!=-1){
            String uri = "https://firebasestorage.googleapis.com/v0/b/pistachio-8f641.appspot.com/o/images%2F"+Integer.toString(movieID)+".jpg?alt=media&token=baff526a-ac90-4390-84ac-da4b9ee0f29a";
            Picasso.with(this).load(uri).into(movieImage);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference MovieRef = database.getReference().child("Movies").child(Integer.toString(movieID));

            MovieRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    movie_title = (String)dataSnapshot.child("title").getValue();
                    movie_genre = (String)dataSnapshot.child("genre").getValue();
                    imdb_rating = (String)dataSnapshot.child("imdb_rating").getValue();
                    movie_actors = (String)dataSnapshot.child("actors").getValue();
                    movie_director = (String)dataSnapshot.child("director").getValue();
                    movie_country = (String)dataSnapshot.child("country").getValue();
                    movie_language = (String)dataSnapshot.child("language").getValue();
                    movie_duration = (String)dataSnapshot.child("runtime").getValue();
                    movie_awards = (String)dataSnapshot.child("awards").getValue();
                    movie_plot = (String)dataSnapshot.child("plot").getValue();

                    if(movie_title!=null){
                        title.setText(movie_title);
                    }
                    else
                        title.setText("N/A");

                    if(movie_genre!=null){
                        genre.setText(movie_genre);
                    }
                    else
                        genre.setText("Other");

                    if(imdb_rating!=null){
                        imdb.setText("IMDB: " + imdb_rating);
                    }
                    else
                        imdb.setText("IMDB: N/A");

                    TitleCreator titleCreator = TitleCreator.get(mContext);
                    List<MovieTitleParent> titles = titleCreator.getAll();
                    if(movie_actors!=null){
                        List<Object> childList = new ArrayList<>();
                        childList.add(new MovieTitleChild(movie_actors));
                        titles.get(0).setChildObjectList(childList);
                    }
                    else {
                        List<Object> childList = new ArrayList<>();
                        childList.add(new MovieTitleChild("N/A"));
                        titles.get(0).setChildObjectList(childList);
                    }

                    if(movie_director!=null){
                        List<Object> childList = new ArrayList<>();
                        childList.add(new MovieTitleChild(movie_director));
                        titles.get(1).setChildObjectList(childList);
                    }
                    else {
                        List<Object> childList = new ArrayList<>();
                        childList.add(new MovieTitleChild("N/A"));
                        titles.get(1).setChildObjectList(childList);
                    }

                    if(movie_country!=null){
                        List<Object> childList = new ArrayList<>();
                        childList.add(new MovieTitleChild(movie_country));
                        titles.get(2).setChildObjectList(childList);
                    }
                    else {
                        List<Object> childList = new ArrayList<>();
                        childList.add(new MovieTitleChild("N/A"));
                        titles.get(2).setChildObjectList(childList);
                    }

                    if(movie_language!=null){
                        List<Object> childList = new ArrayList<>();
                        childList.add(new MovieTitleChild(movie_language));
                        titles.get(3).setChildObjectList(childList);
                    }
                    else {
                        List<Object> childList = new ArrayList<>();
                        childList.add(new MovieTitleChild("N/A"));
                        titles.get(3).setChildObjectList(childList);
                    }

                    if(movie_duration!=null){
                        List<Object> childList = new ArrayList<>();
                        childList.add(new MovieTitleChild(movie_duration));
                        titles.get(4).setChildObjectList(childList);
                    }
                    else {
                        List<Object> childList = new ArrayList<>();
                        childList.add(new MovieTitleChild("N/A"));
                        titles.get(4).setChildObjectList(childList);
                    }

                    if(movie_awards!=null){
                        List<Object> childList = new ArrayList<>();
                        childList.add(new MovieTitleChild(movie_awards));
                        titles.get(5).setChildObjectList(childList);
                    }
                    else {
                        List<Object> childList = new ArrayList<>();
                        childList.add(new MovieTitleChild("N/A"));
                        titles.get(5).setChildObjectList(childList);
                    }

                    if(movie_plot!=null){
                        List<Object> childList = new ArrayList<>();
                        childList.add(new MovieTitleChild(movie_plot));
                        titles.get(6).setChildObjectList(childList);
                    }
                    else {
                        List<Object> childList = new ArrayList<>();
                        childList.add(new MovieTitleChild("N/A"));
                        titles.get(6).setChildObjectList(childList);
                    }

                    for(MovieTitleParent title:titles)
                        parentObject.add(title);

                    MovieAdapter movieAdapter = new MovieAdapter(mContext, parentObject);
                    movieAdapter.setParentClickableViewAnimationDefaultDuration();
                    movieAdapter.setParentAndIconExpandOnClick(true);
                    recyclerView.setAdapter(movieAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                // User is signed in
                DatabaseReference userRatingRef = database.getReference().child("Users").child(user.getUid()).child("Ratings");
                DatabaseReference userBookmarkRef = database.getReference().child("Users").child(user.getUid()).child("Bookmarks");
                userRatingRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        DataSnapshot ratingRef = dataSnapshot.child(Integer.toString(movieID));
                        if(ratingRef.getValue()!=null)
                            ratingBar.setRating(Float.parseFloat(ratingRef.getValue().toString()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                userBookmarkRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        DataSnapshot bookmarkRef = dataSnapshot.child(Integer.toString(movieID));
                        if(bookmarkRef.getValue()!=null) {
                            bookmark_flag = true;
                            bookmark.setImageResource(R.drawable.bookmark_fill);
                        }
                        else {
                            bookmark_flag = false;
                            bookmark.setImageResource(R.drawable.bookmark_border);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            } else {
                // No user is signed in
            }
        }
        else{

        }

    }
}
