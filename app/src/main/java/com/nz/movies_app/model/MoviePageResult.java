package com.nz.movies_app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.nz.movies_app.utils.AppUtil;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("ALL")
public class MoviePageResult implements Parcelable {
    private transient long id;
    @SerializedName("page")
    private int page;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("results")
    private ArrayList<Movie> movieResult;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public ArrayList<Movie> getMovieResult() {
        return movieResult;
    }

    public void setMovieResult(ArrayList<Movie> movieResult) {
        this.movieResult = movieResult;
    }

    protected MoviePageResult(Parcel in) {
        id = AppUtil.getRandomNumber();
        page = in.readInt();
        totalResults = in.readInt();
        totalPages = in.readInt();
        movieResult = in.createTypedArrayList(Movie.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(page);
        dest.writeInt(totalResults);
        dest.writeInt(totalPages);
        dest.writeTypedList(movieResult);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MoviePageResult> CREATOR = new Creator<MoviePageResult>() {
        @Override
        public MoviePageResult createFromParcel(Parcel in) {
            return new MoviePageResult(in);
        }

        @Override
        public MoviePageResult[] newArray(int size) {
            return new MoviePageResult[size];
        }
    };
}