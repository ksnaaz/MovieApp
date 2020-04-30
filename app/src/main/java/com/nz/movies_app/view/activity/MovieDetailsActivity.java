package com.nz.movies_app.view.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.nz.movies_app.R;
import com.nz.movies_app.databinding.ActivityMovieDetailsBinding;
import com.nz.movies_app.model.MovieDetails;
import com.nz.movies_app.utils.AppUtil;
import com.nz.movies_app.view_model.MovieDetailsViewModel;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {
    private ActivityMovieDetailsBinding movieDetailsBinding;
    private MovieDetailsViewModel movieDetailsViewModel;
    private int movieId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieDetailsViewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel.class);
        movieDetailsBinding = DataBindingUtil.setContentView(MovieDetailsActivity.this, R.layout.activity_movie_details);
        movieDetailsBinding.setLifecycleOwner(this);
        movieDetailsBinding.setMovieDetailsViewModel(movieDetailsViewModel);
        movieId = getIntent().getIntExtra("movieId", 0);
        movieDetailsViewModel.getMovieDetails(movieId).observe(this, new Observer<MovieDetails>() {
            @Override
            public void onChanged(@Nullable MovieDetails moviesResult) {
                Log.e("Movie Title ", moviesResult.getTitle());
                setDataInView(moviesResult);
            }
        });
    }

    private void setDataInView(MovieDetails movieDetails) {
        Picasso.with(movieDetailsBinding.bannerImage.getContext()).load(AppUtil.movieImagePathBuilder(movieDetails.getPoster_path())).placeholder(R.drawable.movie_detail_placeholder).fit().centerCrop().into(movieDetailsBinding.bannerImage);
        movieDetailsBinding.circularProgressbar.setProgress(movieDetails.getVote_average()*10, 100);
        movieDetailsBinding.movieTitle.setText(movieDetails.getTitle());
        movieDetailsBinding.releaseDate.setText("Released on : "+movieDetails.getRelease_date());
        String laguageAvailable = "";
        for (MovieDetails.SpokenLanguagesBean bean : movieDetails.getSpoken_languages()) {
            laguageAvailable = laguageAvailable.isEmpty() ? bean.getName() : laguageAvailable + " , " + bean.getName();
        }
        movieDetailsBinding.languages.setText(laguageAvailable);
        movieDetailsBinding.movieTagline.setVisibility(movieDetails.getTagline() != null && !movieDetails.getTagline().isEmpty() ? View.VISIBLE : View.GONE);
        movieDetailsBinding.movieTagline.setText(movieDetails.getTagline());
        movieDetailsBinding.movieDescription.setText(movieDetails.getOverview());
    }

}
