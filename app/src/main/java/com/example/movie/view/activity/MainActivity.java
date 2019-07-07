package com.example.movie.view.activity;

import android.app.SearchManager;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.movie.R;
import com.example.movie.uitil.AppContants;
import com.example.movie.view.fragment.FileListFragment;
import com.example.movie.view.fragment.SearchFragment;
import com.example.movie.viewmodel.MainController;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavView;

    private MainController mController;
    private SearchView mSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDrawerLayout();

        mController = ViewModelProviders.of(this).get(MainController.class);
        openFileListFragment();
    }

    private void openFileListFragment() {
        FileListFragment listFile = new FileListFragment();
        listFile.setArguments(mController);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, listFile);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initDrawerLayout() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavView = findViewById(R.id.nav_view);
        mNavView.getMenu().getItem(0).setChecked(true);
        mNavView.setNavigationItemSelectedListener(navListener);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();


    }

    @Override
    public void onBackPressed() {
        if(closeDrawer()) return;
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else {
            super.onBackPressed();
        }

    }

    private boolean closeDrawer() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;

        }
        return false;

    }

    NavigationView.OnNavigationItemSelectedListener navListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            int id = menuItem.getItemId();
            switch (id) {
                case R.id.nav_top_rate:
                    mController.loadingPage(AppContants.MovieType.TOP_RATE);
                    menuItem.setChecked(true);
                    closeDrawer();
                    break;
                case R.id.nav_popular:
                    mController.loadingPage(AppContants.MovieType.POPULAR);
                    menuItem.setChecked(true);
                    closeDrawer();
                    break;
                case R.id.nav_upcoming:
                    mController.loadingPage(AppContants.MovieType.UPCOMMING);
                    menuItem.setChecked(true);
                    closeDrawer();
                    break;

            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        Log.d("dong.nd1", "A");
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            mSearchView = (SearchView) searchItem.getActionView();
        }
        if (mSearchView != null) {
            mSearchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startSearch();
                }
            });
            mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.d("dong.nd1", newText);

                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.d("dong.nd1", query);

                    return true;
                }
            };
            mSearchView.setOnQueryTextListener(queryTextListener);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


    private void startSearch(){
        SearchFragment searchFragment = new SearchFragment();
        String backStateName = searchFragment.getClass().getName();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, searchFragment);
        transaction.addToBackStack(backStateName);
        transaction.commit();
    }
}
