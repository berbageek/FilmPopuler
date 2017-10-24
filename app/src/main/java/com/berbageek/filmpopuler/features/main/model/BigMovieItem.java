package com.berbageek.filmpopuler.features.main.model;

import com.berbageek.filmpopuler.R;

/**
 * Created by Muhammad Fiqri Muthohar on 10/20/17.
 */

public class BigMovieItem implements MovieItem {
    private final String movieId;
    private final String movieName;
    private final String posterPath;
    private final String overview;

    public BigMovieItem(String movieId, String movieName, String posterPath, String overview) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.posterPath = posterPath;
        this.overview = overview;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    @Override
    public int getType() {
        return R.layout.main_big_movie_item;
    }

    @Override
    public int getItemSize() {
        return 2;
    }
}
