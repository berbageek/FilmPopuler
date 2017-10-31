package com.berbageek.filmpopuler.features.detail.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.berbageek.filmpopuler.data.db.DatabaseHelper;
import com.berbageek.filmpopuler.data.db.contract.MovieRepository;

/**
 * Created by Muhammad Fiqri Muthohar on 10/26/17.
 */

public class FavoriteMovieLoader extends AsyncTaskLoader<Boolean> {
    public static final int FAVORITE_MOVIE_LOADER_ID = 101;
    private static final String TAG = "FavoriteMovieLoader";
    private MovieRepository movieRepository;
    private Boolean isFavorite = null;
    private String movieId;

    public FavoriteMovieLoader(Context context, String movieId) {
        super(context);
        movieRepository = DatabaseHelper.getInstance(context);
        this.movieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (isFavorite == null) {
            forceLoad();
        } else {
            deliverResult(isFavorite);
        }
    }

    @Override
    public Boolean loadInBackground() {
        try {
            isFavorite = movieRepository.isMovieFavored(movieId);
        } catch (Exception e) {
            Log.e(TAG, "loadInBackground: ", e);
        }
        return isFavorite;
    }
}
