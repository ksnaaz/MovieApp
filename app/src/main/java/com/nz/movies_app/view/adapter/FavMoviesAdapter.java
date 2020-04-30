package com.nz.movies_app.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nz.movies_app.R;
import com.nz.movies_app.model.Movie;
import com.nz.movies_app.utils.AppUtil;
import com.nz.movies_app.utils.ItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavMoviesAdapter extends RecyclerView.Adapter<FavMoviesAdapter.MoviesItemViewHolder> {
    private Context context;
    private List<Movie> moviesArrayList;
    private LayoutInflater mInflater;
    private ItemClickListener itemClickListener;

    public FavMoviesAdapter(Context mContext, ItemClickListener clickListener) {
        this.context = mContext;
        moviesArrayList = new ArrayList<>();
        this.mInflater = LayoutInflater.from(context);
        this.itemClickListener = clickListener;
    }

    @NonNull
    @Override
    public MoviesItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_favmovie_card, viewGroup, false);
        return new MoviesItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesItemViewHolder viewHolder, final int i) {
        viewHolder.cardView.setLayoutParams(new ViewGroup.LayoutParams(getScreenWidth() / 2, getMeasuredPosterHeight(getScreenWidth() / 2)));
        Picasso.with(viewHolder.imageView.getContext()).load(AppUtil.movieImagePathBuilder(moviesArrayList.get(i).getPosterPath())).placeholder(R.drawable.movie_placeholder).fit().centerCrop().into(viewHolder.imageView);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            itemClickListener.onMovieClick(moviesArrayList.get(i).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesArrayList.size();
    }


    public class MoviesItemViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private ImageView imageView;
        private CardView cardView;

        public MoviesItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            imageView = itemView.findViewById(R.id.item_image);
            cardView = itemView.findViewById(R.id.root_view);
        }
    }

    private int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private int getMeasuredPosterHeight(int width) {
        return (int) (width * 1.5f);
    }

    public void addDataIntoList(List<Movie> favouriteMovies) {
        moviesArrayList.clear();
        moviesArrayList.addAll(favouriteMovies);
        notifyDataSetChanged();
        Log.e("Fav Adapter", "(addDataIntoList) size = " + moviesArrayList.size());
    }
}