package com.berbageek.filmpopuler.features.main.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.berbageek.filmpopuler.features.main.model.MainItem;

/**
 * Created by Muhammad Fiqri Muthohar on 10/19/17.
 */

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindView(MainItem item);
}
