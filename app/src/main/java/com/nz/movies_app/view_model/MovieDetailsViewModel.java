package com.nz.movies_app.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.nz.movies_app.model.MovieDetails;
import com.nz.movies_app.rest.RestApiFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nz.movies_app.BaseConstants.API_KEY;

public class MovieDetailsViewModel extends AndroidViewModel {
    private Application application;
    private MutableLiveData<MovieDetails> mutableMovieDetailsLiveData;

    public MovieDetailsViewModel(Application application) {
        super(application);
        this.mutableMovieDetailsLiveData = new MutableLiveData<MovieDetails>();
    }

    public LiveData<MovieDetails> getMovieDetails(int videoId) {
        return getMutableLiveDataOfMovieDetail(videoId);
    }

    public MutableLiveData<MovieDetails> getMutableLiveDataOfMovieDetail(int id) {

        Call<MovieDetails> call = RestApiFactory.create().getMovieDetails(id,API_KEY);
        call.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                MovieDetails movie = response.body();
                if (movie != null) {
                    mutableMovieDetailsLiveData.setValue(movie);
                }
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) { }
        });
        return mutableMovieDetailsLiveData;
    }
}
