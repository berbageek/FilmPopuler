package com.berbageek.filmpopuler.features.detail;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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
import com.berbageek.filmpopuler.data.db.DatabaseHelper;
import com.berbageek.filmpopuler.data.db.contract.MovieRepository;
import com.berbageek.filmpopuler.data.model.MovieData;
import com.berbageek.filmpopuler.data.model.MovieDetail;
import com.berbageek.filmpopuler.features.detail.loader.FavoriteMovieLoader;
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

    FloatingActionButton favoriteButton;

    ViewGroup detailWrapperView;

    String movieId;
    String movieTitle;
    String posterPath;
    MovieData movieData;
    MovieDetail movieDetail;

    AppBarLayout appBarLayout;

    SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyy", Locale.getDefault());
    AddOrDeleteFavoriteMovieTask addOrDeleteFavoriteMovieTask;
    private boolean isTitleVisible = false;
    private boolean isTitleContainerVisible = true;
    private boolean isFavored = false;
    LoaderManager.LoaderCallbacks<Boolean> favoriteMovieCallback = new LoaderManager.LoaderCallbacks<Boolean>() {
        @Override
        public Loader<Boolean> onCreateLoader(int i, Bundle bundle) {
            return new FavoriteMovieLoader(MovieDetailActivity.this, movieId);
        }

        @Override
        public void onLoadFinished(Loader<Boolean> loader, Boolean favored) {
            enableFavoriteButton();
            isFavored = favored;
            if (isFavored) {
                setFavoriteImage();
            } else {
                setNonFavoriteImage();
            }
        }

        @Override
        public void onLoaderReset(Loader<Boolean> loader) {
            // do nothing
        }
    };

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (addOrDeleteFavoriteMovieTask != null) {
            addOrDeleteFavoriteMovieTask.cancel(true);
        }
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

        favoriteButton = findViewById(R.id.movie_detail_favorite_button);
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
        disableFavoriteButton();
        getSupportLoaderManager().initLoader(
                FavoriteMovieLoader.FAVORITE_MOVIE_LOADER_ID,
                null,
                favoriteMovieCallback
        );
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addOrDeleteFavoriteMovieTask != null) {
                    addOrDeleteFavoriteMovieTask.cancel(true);
                }
                addOrDeleteFavoriteMovieTask = new AddOrDeleteFavoriteMovieTask(getApplicationContext());
                addOrDeleteFavoriteMovieTask.execute(movieData);
            }
        });
    }

    private void setNonFavoriteImage() {
        if (favoriteButton != null) {
            favoriteButton.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }
    }

    private void setFavoriteImage() {
        if (favoriteButton != null) {
            favoriteButton.setImageResource(R.drawable.ic_favorite_white_24dp);
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
                // retry fetch movie detail
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

    private void enableFavoriteButton() {
        if (favoriteButton != null) {
            favoriteButton.setEnabled(true);
        }
    }

    private void disableFavoriteButton() {
        if (favoriteButton != null) {
            favoriteButton.setEnabled(false);
        }
    }

    private class AddOrDeleteFavoriteMovieTask extends AsyncTask<MovieData, Void, Boolean> {
        MovieRepository movieRepository;

        AddOrDeleteFavoriteMovieTask(Context context) {
            movieRepository = DatabaseHelper.getInstance(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            disableFavoriteButton();
        }

        @Override
        protected Boolean doInBackground(MovieData... params) {
            boolean result = false;
            final MovieData data = params[0];
            if (isFavored) {
                movieRepository.removeFavoriteMovie(String.valueOf(data.getId()));
            } else {
                movieRepository.addFavoriteMovie(data);
                result = true;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (!isCancelled()) {
                isFavored = result;
                enableFavoriteButton();
                if (result) {
                    setFavoriteImage();
                } else {
                    setNonFavoriteImage();
                }
            }
        }
    }
}
