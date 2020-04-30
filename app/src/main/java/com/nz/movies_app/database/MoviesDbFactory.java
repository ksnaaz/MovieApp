package com.nz.movies_app.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.nz.movies_app.model.Movie;

import java.util.List;

/**
 * Created by hp on 4/27/2020.
 */

public class MoviesDbFactory {
    private FavoriteMoviesDao favoriteMoviesDao;
    private LiveData<List<Movie>> mAllFavItems;

    public MoviesDbFactory(Application appController){
        MoviesDatabase database = MoviesDatabase.getDatabaseInstance(appController.getApplicationContext());
        favoriteMoviesDao = database.moviesDao();
        mAllFavItems = favoriteMoviesDao.getAllFavMovies();
    }

    public void addItem(final Movie movie){
        insertDeleteUser(movie, true);
    }

    public void deleteItem(final Movie movie){
        insertDeleteUser(movie, false);
    }

    public LiveData<List<Movie>> getAllFavMovies() {
        return mAllFavItems;
    }

    public void insertDeleteUser(final Movie movies, final boolean isAdd) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if(isAdd) {
                    favoriteMoviesDao.addFavItems(movies);
                } else {
                    favoriteMoviesDao.deleteFavItems(movies);
                }
                return null;
            }
        }.execute();
    }
}
