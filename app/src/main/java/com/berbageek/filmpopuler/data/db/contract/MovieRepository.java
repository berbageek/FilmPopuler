package com.berbageek.filmpopuler.data.db.contract;

import com.berbageek.filmpopuler.data.model.MovieData;

import java.util.List;

/**
 * Created by Muhammad Fiqri Muthohar on 10/24/17.
 */

public interface MovieRepository {
    List<MovieData> getFavoriteMovie();

    void addFavoriteMovie(MovieData movieData);

    boolean isMovieFavored(String movieId);

    void updateFavoriteMovie(MovieData movieData);

    void removeFavoriteMovie(String movieId);
}
