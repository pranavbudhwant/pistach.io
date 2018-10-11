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

    public item(Parcel in){
        this.uri = in.readString();
        this.movie_id = in.readInt();
        this.initialRating = in.readFloat();
        this.fragment = in.readString();
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
        dest.writeString(this.uri);
        dest.writeInt(this.movie_id);
        dest.writeFloat(this.initialRating);
        dest.writeString(this.fragment);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<item>(){
        public item createFromParcel(Parcel in){
            return new item(in);
        }
        public item[] newArray(int size){
            return new item[size];
        }
    };
}
