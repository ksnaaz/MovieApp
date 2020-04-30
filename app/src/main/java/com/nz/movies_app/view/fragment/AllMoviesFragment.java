package com.nz.movies_app.view.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nz.movies_app.R;
import com.nz.movies_app.databinding.FragmentAllmoviesBinding;
import com.nz.movies_app.model.Movie;
import com.nz.movies_app.model.MoviePageResult;
import com.nz.movies_app.utils.FavouriteClickListener;
import com.nz.movies_app.utils.ItemClickListener;
import com.nz.movies_app.utils.NetworkState;
import com.nz.movies_app.view.activity.MovieDetailsActivity;
import com.nz.movies_app.view.adapter.AllMoviesAdapter;
import com.nz.movies_app.view_model.AllMoviesViewModel;

/**
 * Created by hp on 4/26/2020.
 */

public class AllMoviesFragment extends Fragment implements FavouriteClickListener, ItemClickListener {
    public static final String TAG = AllMoviesFragment.class.getSimpleName();
    private FragmentAllmoviesBinding fragmentAllmoviesBinding;
    private AllMoviesAdapter allMoviesAdapter;
    private AllMoviesViewModel allMoviesViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        fragmentAllmoviesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_allmovies, container, false);
        return fragmentAllmoviesBinding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        allMoviesViewModel = ViewModelProviders.of(getActivity()).get(AllMoviesViewModel.class);

        fragmentAllmoviesBinding.rvMovies.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        allMoviesAdapter = new AllMoviesAdapter(getActivity(), this, this);
        startListening();
        allMoviesViewModel.getFilter().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                allMoviesViewModel.replaceSubscription(AllMoviesFragment.this, s);
                startListening();
            }
        });

        fragmentAllmoviesBinding.rvMovies.setAdapter(allMoviesAdapter);
    }

    @Override
    public void onFavClick(Movie movie, boolean isFavourite) {
        if (isFavourite) {
            allMoviesViewModel.addFavouriteMovie(movie);
        } else {
            allMoviesViewModel.deleteFavouriteMovie(movie);
        }
    }

    @Override
    public void onMovieClick(int movieId) {
        Intent i = new Intent(getActivity(), MovieDetailsActivity.class);
        i.putExtra("movieId", movieId);
        startActivity(i);
    }

    private void startListening() {
        allMoviesViewModel.getTopRatedMoviesLiveData().observe(this, new Observer<PagedList<Movie>>() {
            @Override
            public void onChanged(@Nullable PagedList<Movie> pagedList) {
                allMoviesAdapter.submitList(pagedList);
            }
        });
        allMoviesViewModel.getNetworkState().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                allMoviesAdapter.setNetworkState(networkState);
            }
        });
    }

}
