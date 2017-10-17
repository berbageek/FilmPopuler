package com.berbageek.filmpopuler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.berbageek.filmpopuler.data.api.TmdbService;
import com.berbageek.filmpopuler.data.model.MovieData;
import com.berbageek.filmpopuler.data.model.MovieDataResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView textField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textField = (TextView) findViewById(R.id.text_field);

        Call<MovieDataResponse> call = TmdbService.open().getMostPopularMovies(1);
        call.enqueue(new Callback<MovieDataResponse>() {
            @Override
            public void onResponse(Call<MovieDataResponse> call, Response<MovieDataResponse> response) {
                MovieDataResponse data = response.body();
                if (data != null) {
                    List<MovieData> movies = data.getMovieDataList();
                    if (movies != null && !movies.isEmpty()) {
                        textField.setText(movies.get(0).toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieDataResponse> call, Throwable t) {
                textField.setText(t.getMessage());
            }
        });
    }
}
