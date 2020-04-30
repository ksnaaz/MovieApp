package com.nz.movies_app.view.adapter;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.nz.movies_app.R;
import com.nz.movies_app.databinding.MoviesItemBinding;
import com.nz.movies_app.databinding.NetworkItemBinding;
import com.nz.movies_app.model.Movie;
import com.nz.movies_app.utils.AppUtil;
import com.nz.movies_app.utils.FavouriteClickListener;
import com.nz.movies_app.utils.ItemClickListener;
import com.nz.movies_app.utils.NetworkState;
import com.squareup.picasso.Picasso;

@SuppressWarnings("ALL")
public class AllMoviesAdapter extends PagedListAdapter<Movie, RecyclerView.ViewHolder> {

    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM = 1;

    private Context context;
    private NetworkState networkState;
    private FavouriteClickListener favouriteClickListener;
    private ItemClickListener itemClickListener;

    public AllMoviesAdapter(Context context, FavouriteClickListener favClickListener, ItemClickListener itemClickListener) {
        super(Movie.DIFF_CALLBACK);
        this.context = context;
        this.favouriteClickListener = favClickListener;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if(viewType == TYPE_PROGRESS) {
            NetworkItemBinding headerBinding = NetworkItemBinding.inflate(layoutInflater, parent, false);
            NetworkStateItemViewHolder viewHolder = new NetworkStateItemViewHolder(headerBinding);
            return viewHolder;

        } else {
            MoviesItemBinding itemBinding = MoviesItemBinding.inflate(layoutInflater, parent, false);
            MoviesItemViewHolder viewHolder = new MoviesItemViewHolder(itemBinding);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MoviesItemViewHolder) {
            ((MoviesItemViewHolder)holder).bindTo(getItem(position));
        } else {
            ((NetworkStateItemViewHolder) holder).bindView(networkState);
        }
    }


    private boolean hasExtraRow() {
        if (networkState != null && networkState != NetworkState.LOADED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return TYPE_PROGRESS;
        } else {
            return TYPE_ITEM;
        }
    }

    public void setNetworkState(NetworkState newNetworkState) {
        NetworkState previousState = this.networkState;
        boolean previousExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }


    public class MoviesItemViewHolder extends RecyclerView.ViewHolder {

        private MoviesItemBinding binding;
        public MoviesItemViewHolder(MoviesItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindTo(final Movie movie) {
            binding.rootView.setLayoutParams(new ViewGroup.LayoutParams(getScreenWidth()/2, getMeasuredPosterHeight(getScreenWidth()/2)));
            Picasso.with(binding.itemImage.getContext()).load(AppUtil.movieImagePathBuilder(movie.getPosterPath())).placeholder(R.drawable.movie_placeholder).fit().centerCrop().into(binding.itemImage);
            binding.favIcon.setImageDrawable(context.getResources().getDrawable(movie.isFavorite() ? R.drawable.fav_selected : R.drawable.fav_unselected));
            binding.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onMovieClick(movie.getId());
                }
            });
            binding.favIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isAlreadyFavorite = movie.isFavorite();
                    movie.setFavorite(!isAlreadyFavorite);
                    binding.favIcon.setImageDrawable(context.getResources().getDrawable(isAlreadyFavorite ? R.drawable.fav_unselected : R.drawable.fav_selected));
                    favouriteClickListener.onFavClick(movie, !isAlreadyFavorite);
                }
            });
        }
    }

    public class NetworkStateItemViewHolder extends RecyclerView.ViewHolder {

        private NetworkItemBinding binding;
        public NetworkStateItemViewHolder(NetworkItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(NetworkState networkState) {
            if (networkState != null && networkState.getStatus() == NetworkState.Status.RUNNING) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }

            if (networkState != null && networkState.getStatus() == NetworkState.Status.FAILED) {
                binding.errorMsg.setVisibility(View.VISIBLE);
                binding.errorMsg.setText(networkState.getMsg());
            } else {
                binding.errorMsg.setVisibility(View.GONE);
            }
        }
    }

    private int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
    private int getMeasuredPosterHeight(int width) {
        return (int) (width * 1.5f);
    }

}