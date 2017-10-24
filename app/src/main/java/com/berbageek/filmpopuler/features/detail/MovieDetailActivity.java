package com.berbageek.filmpopuler.features.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.berbageek.filmpopuler.R;
import com.berbageek.filmpopuler.data.api.TmdbConstant;
import com.berbageek.filmpopuler.data.api.TmdbService;
import com.berbageek.filmpopuler.data.model.MovieData;
import com.berbageek.filmpopuler.data.model.MovieDetail;
import com.berbageek.filmpopuler.utils.AnimationUtils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener {

    public static final String KEY_MOVIE_ID = "MOVIE_ID";
    public static final String KEY_MOVIE_TITLE = "MOVIE_TITLE";
    public static final String KEY_POSTER_PATH = "POSTER_PATH";
    public static final String KEY_MOVIE_DATA = "MOVIE_DATA";
    private static final String TAG = "MovieDetailActivity";
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.75f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.75f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private static final float SCRIM_ADJUSTMENT = 0.075f;

    TextView movieTitleField;
    TextView movieRatingField;
    TextView movieReleaseDateField;
    TextView movieDetailTaglineField;
    TextView movieDetailDurationField;
    TextView movieDetailOverviewField;
    TextView toolbarTitleView;

    ImageView moviePosterView;
    ImageView movieBackdropView;

    Toolbar toolbar;

    ViewGroup detailWrapperView;

    String movieId;
    String movieTitle;
    String posterPath;
    MovieData movieData;
    MovieDetail movieDetail;

    AppBarLayout appBarLayout;

    SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyy", Locale.getDefault());
    private boolean isTitleVisible = false;
    private boolean isTitleContainerVisible = true;

    public static void showMovieDetailPage(Context context, String movieId,
                                           String movieTitle, String posterPath,
                                           Parcelable parcelableMovieData) {
        Intent detailIntent = new Intent(context, MovieDetailActivity.class);
        detailIntent.putExtra(KEY_MOVIE_ID, movieId);
        detailIntent.putExtra(KEY_MOVIE_TITLE, movieTitle);
        detailIntent.putExtra(KEY_POSTER_PATH, posterPath);
        detailIntent.putExtra(KEY_MOVIE_DATA, parcelableMovieData);
        context.startActivity(detailIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        viewBinding();
        processIntent();
        setUpToolbar();
        setUpDetails();
        setUpMovieDetail();
    }

    private void processIntent() {
        if (getIntent() != null) {
            movieId = getIntent().getStringExtra(KEY_MOVIE_ID);
            movieTitle = getIntent().getStringExtra(KEY_MOVIE_TITLE);
            posterPath = getIntent().getStringExtra(KEY_POSTER_PATH);
            movieData = getIntent().getParcelableExtra(KEY_MOVIE_DATA);
        }
    }

    private void viewBinding() {
        toolbar = findViewById(R.id.movie_detail_toolbar);
        appBarLayout = findViewById(R.id.movie_detail_appbar);

        toolbarTitleView = findViewById(R.id.movie_detail_toolbar_title);
        movieTitleField = findViewById(R.id.movie_detail_title);
        movieRatingField = findViewById(R.id.movie_detail_ratings);
        movieReleaseDateField = findViewById(R.id.movie_detail_release_date);
        movieDetailTaglineField = findViewById(R.id.movie_detail_tagline_field);
        movieDetailDurationField = findViewById(R.id.movie_detail_duration_field);
        movieDetailOverviewField = findViewById(R.id.movie_detail_overview_field);

        detailWrapperView = findViewById(R.id.movie_detail_wrapper);

        moviePosterView = findViewById(R.id.movie_detail_poster_image);
        movieBackdropView = findViewById(R.id.movie_detail_backdrop_image);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appBarLayout.addOnOffsetChangedListener(this);
        getSupportActionBar().setTitle("");
        toolbarTitleView.setText(movieTitle);
    }

    private void setUpDetails() {
        movieTitleField.setText(movieTitle);
        if (movieData != null) {
            movieDetailOverviewField.setText(movieData.getOverview());
            Date movieReleaseDate;
            try {
                movieReleaseDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US)
                        .parse(movieData.getReleaseDate());
            } catch (Exception e) {
                movieReleaseDate = Calendar.getInstance().getTime();
            }
            movieReleaseDateField.setText(String.format(
                    "Release date : %s",
                    dateFormat.format(movieReleaseDate))
            );
            movieRatingField.setText(String.format(Locale.getDefault(), "%.1f", movieData.getVoteAverage()));
            Picasso.with(this)
                    .load(TmdbConstant.IMAGE_BASE_URL + "w342/" + movieData.getPosterPath())
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(moviePosterView);
            Picasso.with(this)
                    .load(TmdbConstant.IMAGE_BASE_URL + "w780/" + movieData.getBackdropPath())
                    .into(movieBackdropView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        // code from https://github.com/saulmm/CoordinatorBehaviorExample
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void getMovieDetail() {
        Call<MovieDetail> call = TmdbService.open().getMovieDetail(movieId);
        call.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                movieDetail = response.body();
                setUpMovieDetail();
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable throwable) {
                // do nothing
                getMovieDetail();
            }
        });
    }

    private void setUpMovieDetail() {
        if (movieDetail != null) {
            movieDetailDurationField.setText(String.format(
                    Locale.getDefault(),
                    "%d minute(s)",
                    movieDetail.getRuntime())
            );
            movieDetailTaglineField.setText(TextUtils.isEmpty(movieDetail.getTagline())
                    ? "-" : movieDetail.getTagline());
        } else {
            getMovieDetail();
        }
    }

    // modified code from https://github.com/saulmm/CoordinatorBehaviorExample
    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!isTitleVisible) {
                AnimationUtils.startAlphaAnimation(
                        toolbarTitleView,
                        ALPHA_ANIMATIONS_DURATION,
                        View.VISIBLE
                );
                changeToolbarColorAlpha(255);
                isTitleVisible = true;
            }
        } else {
            if (isTitleVisible) {
                AnimationUtils.startAlphaAnimation(
                        toolbarTitleView,
                        ALPHA_ANIMATIONS_DURATION,
                        View.INVISIBLE
                );
                changeToolbarColorAlpha(0);
                isTitleVisible = false;
            }
        }
    }

    private void changeToolbarColorAlpha(int alpha) {
        if (toolbar.getBackground() != null) {
            toolbar.getBackground().setAlpha(alpha);
        }
    }

    // modified code from https://github.com/saulmm/CoordinatorBehaviorExample
    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (isTitleContainerVisible) {
                AnimationUtils.startAlphaAnimation(
                        detailWrapperView,
                        ALPHA_ANIMATIONS_DURATION,
                        View.INVISIBLE
                );
                isTitleContainerVisible = false;
            }
        } else {
            if (!isTitleContainerVisible) {
                AnimationUtils.startAlphaAnimation(
                        detailWrapperView,
                        ALPHA_ANIMATIONS_DURATION,
                        View.VISIBLE
                );
                isTitleContainerVisible = true;
            }
        }
    }
}
