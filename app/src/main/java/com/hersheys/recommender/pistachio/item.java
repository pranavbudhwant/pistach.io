package com.hersheys.recommender.pistachio;

public class item {

    int background;
    String title;
    String genres;
    String rating;
    String uri;

    public item(int background, String title, String genres, String rating, String uri) {
        this.background = background;
        this.title = title;
        this.genres = genres;
        this.rating = rating;
        this.uri = uri;
    }

    public int getBackground() {
        return background;
    }

    public String getTitle() {
        return title;
    }

    public String getGenres(){return genres;}

    public String getRating(){return rating;}

    public String getUri(){return uri;}

    public void setBackground(int background) {
        this.background = background;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenres(String genres){this.genres = genres;}

    public void setRating(String rating){this.rating = rating;}

    public void setUri(String uri){this.uri = uri;}
}
