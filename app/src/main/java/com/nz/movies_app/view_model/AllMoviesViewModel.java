package com.nz.movies_app.view_model;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.nz.movies_app.database.MoviesDbFactory;
import com.nz.movies_app.datasource.MoviesDataSource;
import com.nz.movies_app.datasource.factory.MoviesDataFactory;
import com.nz.movies_app.model.Movie;
import com.nz.movies_app.model.MovieDetails;
import com.nz.movies_app.model.MoviePageResult;
import com.nz.movies_app.rest.RestApiFactory;
import com.nz.movies_app.utils.NetworkState;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nz.movies_app.BaseConstants.API_KEY;

/**
 * Created by hp on 4/26/2020.
 */

public class AllMoviesViewModel extends AndroidViewModel {

    private Executor executor;
    private LiveData<NetworkState> networkState;
    private LiveData<PagedList<Movie>> topRatedMoviesLiveData;
    private MutableLiveData<String> getFilter;
    private MoviesDbFactory moviesDbFactory;
    private Application application;
    private MoviesDataFactory moviesDataFactory;

    public AllMoviesViewModel(Application application) {
        super(application);
        moviesDbFactory = new MoviesDbFactory(application);
        getFilter = new MutableLiveData<>();
        init("");
    }

    public void init(String searchQuery) {
        executor = Executors.newFixedThreadPool(5);

        moviesDataFactory = new MoviesDataFactory(application, searchQuery);
        networkState = Transformations.switchMap(moviesDataFactory.getMutableLiveData(),
                new Function<MoviesDataSource, LiveData<NetworkState>>() {
                    @Override
                    public LiveData<NetworkState> apply(MoviesDataSource dataSource) {
                        return dataSource.getNetworkState();
                    }
                });

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(true)
                        .setInitialLoadSizeHint(10)
                        .setPageSize(20).build();

        topRatedMoviesLiveData = (new LivePagedListBuilder(moviesDataFactory, pagedListConfig))
                .setFetchExecutor(executor)
                .build();
    }


    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public LiveData<PagedList<Movie>> getTopRatedMoviesLiveData() {
        return topRatedMoviesLiveData;
    }

    public void addFavouriteMovie(Movie movie) {
        moviesDbFactory.addItem(movie);
    }

    public void deleteFavouriteMovie(Movie movie) {
        moviesDbFactory.deleteItem(movie);
    }

    public LiveData<String > getFilter() {
        return getFilter;
    }

    public void setSearchQuery(String searchQuery){
        getFilter.setValue(searchQuery);
    }

    public void replaceSubscription(LifecycleOwner lifecycleOwner, String searchQuery) {
        moviesDataFactory.getMutableLiveData().getValue().invalidate();
        topRatedMoviesLiveData.removeObservers(lifecycleOwner);
        init(searchQuery);
    }

}
