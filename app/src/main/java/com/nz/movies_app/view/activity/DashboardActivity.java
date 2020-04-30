package com.nz.movies_app.view.activity;

import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.nz.movies_app.R;
import com.nz.movies_app.databinding.ActivityDashboardBinding;
import com.nz.movies_app.view.adapter.TabLayoutAdapter;
import com.nz.movies_app.view_model.AllMoviesViewModel;

public class DashboardActivity extends AppCompatActivity {
    private AllMoviesViewModel allMoviesViewModel;
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDashboardBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        setSupportActionBar(binding.toolbar);
        allMoviesViewModel = ViewModelProviders.of(this).get(AllMoviesViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setHandler(this);
        binding.setManager(getSupportFragmentManager());

    }

    @BindingAdapter({"bind:handler"})
    public static void bindViewPagerAdapter(final ViewPager view, final DashboardActivity activity) {
        final TabLayoutAdapter adapter = new TabLayoutAdapter(view.getContext(), activity.getSupportFragmentManager());
        view.setAdapter(adapter);
    }

    @BindingAdapter({"bind:pager"})
    public static void bindViewPagerTabs(final TabLayout view, final ViewPager pagerView) {
        view.setupWithViewPager(pagerView, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                allMoviesViewModel.setSearchQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (searchView.getQuery().length() == 0) {
                    allMoviesViewModel.setSearchQuery("");
                }
                return false;
            }
        });

        return true;
    }


}