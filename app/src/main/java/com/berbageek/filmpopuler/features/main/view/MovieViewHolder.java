package com.berbageek.filmpopuler.features.main.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.berbageek.filmpopuler.R;
import com.berbageek.filmpopuler.data.api.TmdbConstant;
import com.berbageek.filmpopuler.features.main.contract.MovieItemClickListener;
import com.berbageek.filmpopuler.features.main.model.MainItem;
import com.berbageek.filmpopuler.features.main.model.MovieItem;
import com.squareup.picasso.Picasso;

/**
 * Created by Muhammad Fiqri Muthohar on 10/19/17.
 */

public class MovieViewHolder extends BaseViewHolder {

    ImageView posterImageView;

    MovieItemClickListener listener;

    public MovieViewHolder(View itemView, MovieItemClickListener itemListener) {
        super(itemView);
        this.listener = itemListener;

        setupViews(itemView);
    }

    private void setupViews(View itemView) {
        posterImageView = itemView.findViewById(R.id.movie_item_poster_image);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = getAdapterPosition();
                if (listener != null && pos != RecyclerView.NO_POSITION) {
                    listener.onMovieItemClicked(pos);
                }
            }
        });
    }

    @Override
    public void bindView(MainItem item) {
        MovieItem movieItem = (MovieItem) item;
        Picasso.with(itemView.getContext())
                .load(TmdbConstant.IMAGE_BASE_URL + "w185/" + movieItem.getPosterPath())
                .placeholder(R.drawable.ic_image_black_24dp)
                .error(R.drawable.ic_broken_image_black_24dp)
                .into(posterImageView);
    }
}
