package com.nz.movies_app.rest;

import com.nz.movies_app.model.MovieDetails;
import com.nz.movies_app.model.MoviePageResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApi {

    @GET("movie/top_rated")
    Call<MoviePageResult> getTopRatedMovies(@Query("page") int page, @Query("api_key") String userkey);

    @GET("movie/{path_variable}")
    Call<MovieDetails> getMovieDetails(@Path("path_variable") int id, @Query("api_key") String userkey);

    @GET("search/movie")
    Call<MoviePageResult> getSearchMovies(@Query("page") int page, @Query("api_key") String userkey, @Query("query") String query);

}
