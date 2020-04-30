package com.nz.movies_app.datasource.factory;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.nz.movies_app.datasource.MoviesDataSource;


public class MoviesDataFactory extends DataSource.Factory {

    private MutableLiveData<MoviesDataSource> mutableLiveData;
    private MoviesDataSource movieDataSource;
    private Application application;
    private String searchQuery;

    public MoviesDataFactory(Application application, String searchQuery) {
        this.application = application;
        this.searchQuery = searchQuery;
        this.mutableLiveData = new MutableLiveData<MoviesDataSource>();
    }

    @Override
    public DataSource create() {
        movieDataSource = new MoviesDataSource(application, searchQuery);
        mutableLiveData.postValue(movieDataSource);
        return movieDataSource;
    }

    public MutableLiveData<MoviesDataSource> getMutableLiveData() {
        return mutableLiveData;
    }

}
