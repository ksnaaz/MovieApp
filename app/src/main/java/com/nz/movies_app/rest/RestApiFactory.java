package com.nz.movies_app.rest;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiFactory {

    private static String BASE_URL = "http://api.themoviedb.org/3/";
    private static Retrofit retrofit = null;

    public static RestApi create() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit.create(RestApi.class);
    }
}
