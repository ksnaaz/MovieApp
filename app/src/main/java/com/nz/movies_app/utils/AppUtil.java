package com.nz.movies_app.utils;

import io.reactivex.functions.Action;

/**
 * Created by hp on 4/26/2020.
 */

public class AppUtil {
    public static String movieImagePathBuilder(String imagePath) {
        return "https://image.tmdb.org/t/p/" +
                "w500" +
                imagePath;
    }

    public static long getRandomNumber() {
        long x = (long) ((Math.random() * ((100000 - 0) + 1)) + 0);
        return x;
    }
}
