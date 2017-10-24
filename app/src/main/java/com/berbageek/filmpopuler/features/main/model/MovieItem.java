package com.berbageek.filmpopuler.features.main.model;

import com.berbageek.filmpopuler.data.model.MovieData;

/**
 * Created by Muhammad Fiqri Muthohar on 10/24/17.
 */

public interface MovieItem extends MainItem {
    String getMovieId();

    String getMovieTitle();

    String getPosterPath();

    MovieData getMovieData();
}
