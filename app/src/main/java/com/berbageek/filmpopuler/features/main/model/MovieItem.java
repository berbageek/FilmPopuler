package com.berbageek.filmpopuler.features.main.model;

import com.berbageek.filmpopuler.R;

/**
 * Created by Muhammad Fiqri Muthohar on 10/19/17.
 */

public class MovieItem implements MainItem {
    private final String movieId;
    private final String movieName;
    private final String posterPath;

    public MovieItem(String movieId, String movieName, String posterPath) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.posterPath = posterPath;
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

    @Override
    public int getType() {
        return R.layout.main_movie_item;
    }

    @Override
    public String toString() {
        return "MovieItem{" +
                "movieId='" + movieId + '\'' +
                ", movieName='" + movieName + '\'' +
                ", posterPath='" + posterPath + '\'' +
                '}';
    }
}
