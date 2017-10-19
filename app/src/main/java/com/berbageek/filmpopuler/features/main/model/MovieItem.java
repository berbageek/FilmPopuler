package com.berbageek.filmpopuler.features.main.model;

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
        return TYPE_MOVIE;
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
