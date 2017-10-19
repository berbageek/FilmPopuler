package com.berbageek.filmpopuler.features.main.model;

public class MovieItemBuilder {
    private String movieId;
    private String movieName;
    private String posterImage;

    public MovieItemBuilder setMovieId(String movieId) {
        this.movieId = movieId;
        return this;
    }

    public MovieItemBuilder setMovieName(String movieName) {
        this.movieName = movieName;
        return this;
    }

    public MovieItemBuilder setPosterImage(String posterImage) {
        this.posterImage = posterImage;
        return this;
    }

    public MovieItem createMovieItem() {
        return new MovieItem(movieId, movieName, posterImage);
    }
}