package com.nz.movies_app.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.nz.movies_app.database.MoviesDbFactory;
import com.nz.movies_app.model.Movie;

import java.util.List;

/**
 * Created by hp on 4/26/2020.
 */


public class FavoriteMoviesViewModel extends AndroidViewModel {

    private MoviesDbFactory moviesDbFactory;
    private LiveData<List<Movie>> mFavMovies;

    public FavoriteMoviesViewModel (Application application) {
        super(application);
        moviesDbFactory = new MoviesDbFactory(application);
        mFavMovies = moviesDbFactory.getAllFavMovies();
    }

    public LiveData<List<Movie>> getFavMovies() { return mFavMovies; }

}