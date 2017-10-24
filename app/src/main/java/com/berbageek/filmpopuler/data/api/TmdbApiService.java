package com.berbageek.filmpopuler.data.api;

import com.berbageek.filmpopuler.data.model.MovieDataResponse;
import com.berbageek.filmpopuler.data.model.MovieDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Muhammad Fiqri Muthohar on 10/17/17.
 */

public interface TmdbApiService {
    @GET("movie/popular")
    Call<MovieDataResponse> getMostPopularMovies(@Query("page") int page);

    @GET("movie/{movie_id}")
    Call<MovieDetail> getMovieDetail(@Path("movie_id") String movieId);
}
