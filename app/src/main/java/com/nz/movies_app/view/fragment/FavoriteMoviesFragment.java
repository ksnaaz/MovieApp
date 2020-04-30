package com.nz.movies_app.view.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nz.movies_app.R;
import com.nz.movies_app.databinding.FragmentFavmoviesBinding;
import com.nz.movies_app.model.Movie;
import com.nz.movies_app.utils.ItemClickListener;
import com.nz.movies_app.view.activity.MovieDetailsActivity;
import com.nz.movies_app.view.adapter.FavMoviesAdapter;
import com.nz.movies_app.view_model.FavoriteMoviesViewModel;

import java.util.List;

/**
 * Created by hp on 4/26/2020.
 */

public class FavoriteMoviesFragment extends Fragment implements ItemClickListener {
    private FavoriteMoviesViewModel favoriteMoviesViewModel;
    private FragmentFavmoviesBinding fragmentFavmoviesBinding;
    private FavMoviesAdapter favMoviesAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        fragmentFavmoviesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favmovies, container, false);
        return fragmentFavmoviesBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        favoriteMoviesViewModel = ViewModelProviders.of(this).get(FavoriteMoviesViewModel.class);
        fragmentFavmoviesBinding.rvMovies.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        favMoviesAdapter = new FavMoviesAdapter(getActivity(), this);
        fragmentFavmoviesBinding.rvMovies.setAdapter(favMoviesAdapter);
        favoriteMoviesViewModel.getFavMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> favouriteMovies) {
                favMoviesAdapter.addDataIntoList(favouriteMovies);
            }
        });

    }

    @Override
    public void onMovieClick(int movieId) {
        Intent i = new Intent(getActivity(), MovieDetailsActivity.class);
        i.putExtra("movieId", movieId);
        startActivity(i);
    }
}
