package com.berbageek.filmpopuler.features.main.model;

import com.berbageek.filmpopuler.R;

/**
 * Created by Muhammad Fiqri Muthohar on 10/24/17.
 */

public class HeaderItem implements MainItem {

    public final String headerTitle;

    public HeaderItem(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    @Override
    public int getType() {
        return R.layout.main_header_item;
    }

    @Override
    public int getItemSize() {
        return 2;
    }
}
