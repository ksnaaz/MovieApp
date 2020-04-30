package com.nz.movies_app.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.nz.movies_app.model.Movie;

import java.util.List;

@Dao
public interface FavoriteMoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addFavItems(Movie favMovies);

    @Delete
    void deleteFavItems(Movie favMovies);

    @Query("SELECT * FROM " + "favorite_table" )
    LiveData<List<Movie>> getAllFavMovies();

}