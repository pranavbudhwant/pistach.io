package com.hersheys.recommender.pistachio;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class item implements Parcelable {
    String uri;
    int movie_id;
    float initialRating;
    String fragment;

    public  item(String uri, int movie_id, float initialRating, String fragment){
        this.uri = uri;
        this.movie_id = movie_id;
        this.initialRating = initialRating;
        this.fragment = fragment;
    }

    public String getUri(){return uri;}

    public int getMovieId(){return movie_id;}

    public float getInitialRating(){return initialRating;}

    public String getFragment(){return fragment;}

    public void setUri(String uri){this.uri = uri;}

    public void setMovie_id(int movie_id){this.movie_id = movie_id;}

    public void setInitialRating(float initialRating){this.initialRating = initialRating;}

    public  void setFragment(String fragment){this.fragment = fragment;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
