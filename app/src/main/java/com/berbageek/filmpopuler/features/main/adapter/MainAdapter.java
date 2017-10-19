package com.berbageek.filmpopuler.features.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.berbageek.filmpopuler.R;
import com.berbageek.filmpopuler.features.main.contract.MainListItemClickListener;
import com.berbageek.filmpopuler.features.main.contract.MovieItemClickListener;
import com.berbageek.filmpopuler.features.main.model.MainItem;
import com.berbageek.filmpopuler.features.main.model.MovieItem;
import com.berbageek.filmpopuler.features.main.view.BaseViewHolder;
import com.berbageek.filmpopuler.features.main.view.MovieViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammad Fiqri Muthohar on 10/19/17.
 */

public class MainAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<MainItem> mainItemList;
    private MainListItemClickListener mainListItemClickListener;
    private MovieItemClickListener movieItemClickListener;

    private Context context;

    public MainAdapter(MainListItemClickListener mainListItemClickListener) {
        this.mainListItemClickListener = mainListItemClickListener;
        this.mainItemList = new ArrayList<>();
        setupMovieItemClickListener();
    }

    private void setupMovieItemClickListener() {
        movieItemClickListener = new MovieItemClickListener() {
            @Override
            public void onMovieItemClicked(int position) {
                MovieItem item = ((MovieItem) mainItemList.get(position));
                if (mainListItemClickListener != null) {
                    mainListItemClickListener.onMovieItemClick(item);
                }
            }
        };
    }

    public void setMainItemList(List<MainItem> mainItemList) {
        this.mainItemList = mainItemList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mainItemList.get(position).getType();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == MainItem.TYPE_MOVIE) {
            View view = LayoutInflater
                    .from(viewGroup.getContext())
                    .inflate(R.layout.main_movie_item, viewGroup, false);
            return new MovieViewHolder(view, movieItemClickListener);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder baseViewHolder, int pos) {
        baseViewHolder.bindView(mainItemList.get(pos));
    }

    @Override
    public int getItemCount() {
        return mainItemList.size();
    }
}
