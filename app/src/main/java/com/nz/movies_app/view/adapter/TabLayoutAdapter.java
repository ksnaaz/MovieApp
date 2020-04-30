package com.nz.movies_app.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.nz.movies_app.R;
import com.nz.movies_app.view.fragment.AllMoviesFragment;
import com.nz.movies_app.view.fragment.FavoriteMoviesFragment;

public class TabLayoutAdapter extends FragmentPagerAdapter
    {
        private static final int ALL_MOVIES = 0;
        private static final int FAV_MOVIES = 1;

        private static final int[] TABS = new int[]{ALL_MOVIES, FAV_MOVIES};

        private Context mContext;

        public TabLayoutAdapter(final Context context, final FragmentManager fm)
        {
            super(fm);
            mContext = context.getApplicationContext();
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (TABS[position])
            {
                case ALL_MOVIES:
                    return new AllMoviesFragment();
                case FAV_MOVIES:
                    return new FavoriteMoviesFragment();
            }
            return null;
        }

        @Override
        public int getCount()
        {
            return TABS.length;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            switch (TABS[position])
            {
                case ALL_MOVIES:
                    return mContext.getResources().getString(R.string.all_movies);
                case FAV_MOVIES:
                    return mContext.getResources().getString(R.string.fav_movies);
            }
            return null;
        }
    }