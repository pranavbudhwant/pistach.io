package com.hersheys.recommender.pistachio.Models;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class TitleCreator {
    static TitleCreator _titleCreator;
    List<MovieTitleParent> _titleParents;

    public TitleCreator(Context context) {
        _titleParents = new ArrayList<>();
        MovieTitleParent actors = new MovieTitleParent("Actors");
        MovieTitleParent director = new MovieTitleParent("Director");
        MovieTitleParent country = new MovieTitleParent("Country");
        MovieTitleParent language = new MovieTitleParent("Language");
        MovieTitleParent runtime = new MovieTitleParent("Runtime");
        MovieTitleParent awards = new MovieTitleParent("Awards");
        MovieTitleParent plot = new MovieTitleParent("Plot");

        _titleParents.add(actors);
        _titleParents.add(director);
        _titleParents.add(country);
        _titleParents.add(language);
        _titleParents.add(runtime);
        _titleParents.add(awards);
        _titleParents.add(plot);

    }

    public static TitleCreator get(Context context){
        if(_titleCreator==null)
            _titleCreator  = new TitleCreator(context);
        return  _titleCreator; 
    }

    public List<MovieTitleParent> getAll() {
        return _titleParents;
    }
}
