package com.berbageek.filmpopuler.utils.converter;

import android.support.annotation.NonNull;

import com.berbageek.filmpopuler.data.model.MovieData;
import com.berbageek.filmpopuler.features.main.model.MainItem;
import com.berbageek.filmpopuler.features.main.model.MovieItemBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammad Fiqri Muthohar on 10/19/17.
 */

public class MovieDataToMainItemConverter {
    private MovieDataToMainItemConverter() {
    }

    public static List<MainItem> getMainItemList(@NonNull List<MovieData> movieDataList) {
        List<MainItem> results = new ArrayList<>();
        for (MovieData movieData : movieDataList) {
            results.add(new MovieItemBuilder()
                    .setMovieName(movieData.getTitle())
                    .setMovieId(String.valueOf(movieData.getId()))
                    .setPosterImage(movieData.getPosterPath())
                    .createMovieItem());
        }
        return results;
    }
}
