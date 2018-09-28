package com.hersheys.recommender.pistachio;

public class item {

    int background;
    String title;
    String genres;
    String rating;

    public item(int background, String title, String genres, String rating) {
        this.background = background;
        this.title = title;
        this.genres = genres;
        this.rating = rating;
    }

    public int getBackground() {
        return background;
    }

    public String getTitle() {
        return title;
    }

    public String getGenres(){return genres;}

    public String getRating(){return rating;}

    public void setBackground(int background) {
        this.background = background;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenres(String genres){this.genres = genres;}

    public void setRating(String rating){this.rating = rating;}
}
