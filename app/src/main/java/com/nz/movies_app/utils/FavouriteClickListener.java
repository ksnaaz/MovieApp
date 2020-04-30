package com.nz.movies_app.utils;

import com.nz.movies_app.model.Movie;

@SuppressWarnings("ALL")
public interface FavouriteClickListener {
    void onFavClick(Movie movie, boolean isFavourite);
}