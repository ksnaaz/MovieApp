package com.nz.movies_app.datasource;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import com.nz.movies_app.BaseConstants;
import com.nz.movies_app.model.Movie;
import com.nz.movies_app.model.MoviePageResult;
import com.nz.movies_app.rest.RestApiFactory;
import com.nz.movies_app.utils.NetworkState;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MoviesDataSource extends PageKeyedDataSource<Integer, Movie> implements BaseConstants {

    private static final String TAG = MoviesDataSource.class.getSimpleName();

    private Application application;

    private MutableLiveData networkState;
    private MutableLiveData initialLoading;
    private String searchQuery;

    public MoviesDataSource(Application application, String searchQuery) {
        this.application = application;
        this.searchQuery = searchQuery;
        networkState = new MutableLiveData();
        initialLoading = new MutableLiveData();
    }


    public MutableLiveData getNetworkState() {
        return networkState;
    }

    public MutableLiveData getInitialLoading() {
        return initialLoading;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params,
                            @NonNull final LoadInitialCallback<Integer, Movie> callback) {
        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);

        Log.i(TAG, "(loadInitial) Loading Rang " + "1" + " Count " + params.requestedLoadSize +" searchQuery = "+searchQuery);

        Call<MoviePageResult> call = searchQuery.isEmpty() ? RestApiFactory.create().getTopRatedMovies(1, API_KEY) :
                RestApiFactory.create().getSearchMovies(1, API_KEY, searchQuery);

        call.enqueue(new Callback<MoviePageResult>() {
            @Override
            public void onResponse(Call<MoviePageResult> call, Response<MoviePageResult> response) {
                if (response.isSuccessful()) {
                    callback.onResult(response.body().getMovieResult(), null, 2);
                    initialLoading.postValue(NetworkState.LOADED);
                    networkState.postValue(NetworkState.LOADED);

                } else {
                    initialLoading.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                }
            }

            @Override
            public void onFailure(Call<MoviePageResult> call, Throwable t) {
                String errorMessage = t == null ? "unknown error" : t.getMessage();
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params,
                           @NonNull LoadCallback<Integer, Movie> callback) {
        Log.i(TAG, "(loadBefore) Loading Rang " + params.key + " Count " + params.requestedLoadSize +" searchQuery = "+searchQuery);
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params,
                          @NonNull final LoadCallback<Integer, Movie> callback) {

        Log.i(TAG, "(loadAfter) Loading Rang " + params.key + " Count " + params.requestedLoadSize +" searchQuery = "+searchQuery);

        networkState.postValue(NetworkState.LOADING);

        Call<MoviePageResult> call = searchQuery.isEmpty() ? RestApiFactory.create().getTopRatedMovies(params.key, API_KEY) :
                RestApiFactory.create().getSearchMovies(params.key, API_KEY, searchQuery);

        call.enqueue(new Callback<MoviePageResult>() {
            @Override
            public void onResponse(Call<MoviePageResult> call, Response<MoviePageResult> response) {
                if (response.isSuccessful()) {
                    Integer nextKey = (params.key == response.body().getTotalResults()) ? null : params.key + 1;
                    callback.onResult(response.body().getMovieResult(), nextKey);
                    networkState.postValue(NetworkState.LOADED);

                } else
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
            }

            @Override
            public void onFailure(Call<MoviePageResult> call, Throwable t) {
                String errorMessage = t == null ? "unknown error" : t.getMessage();
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
            }
        });
    }
}
