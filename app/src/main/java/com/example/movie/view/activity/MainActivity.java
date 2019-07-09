package com.example.movie.view.activity;

import android.app.SearchManager;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
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
import com.example.movie.network.NetworkUntil;
import com.example.movie.uitil.AppContants;
import com.example.movie.view.fragment.FileListFragment;
import com.example.movie.view.fragment.SearchFragment;
import com.example.movie.viewmodel.MainController;
import com.example.movie.viewmodel.SearchController;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavView;

    private MainController mController;
    private SearchView mSearchView;
    private int mCurrentListType = AppContants.MovieType.TOP_RATE;
    private SearchController mSearchController;
    private Handler mSearchHandler;
    private BroadcastReceiver mNetworkRecevier;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDrawerLayout();

        mController = ViewModelProviders.of(this).get(MainController.class);
        openFileListFragment();

    }

    private void resigerNetworkReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        mNetworkRecevier = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(NetworkUntil.isConnectedToNetwork(getApplication())){
                    mController.loadingMovie(mCurrentListType, -1);
                }
            }
        };

        registerReceiver(mNetworkRecevier, filter);
    }

    @Override
    protected void onResume() {
        resigerNetworkReceiver();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if(mNetworkRecevier != null)unregisterReceiver(mNetworkRecevier);
        super.onDestroy();
    }

    private void openFileListFragment() {
        FileListFragment listFile = new FileListFragment();
        listFile.setArguments(mController);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, listFile);
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
        if (closeDrawer()) return;
        if (!mSearchView.isIconified())
            mSearchView.onActionViewCollapsed();
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }


    }

    private boolean closeDrawer() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
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
                    mCurrentListType = AppContants.MovieType.TOP_RATE;
                    mController.loadingPage(AppContants.MovieType.TOP_RATE);
                    menuItem.setChecked(true);
                    closeDrawer();
                    break;
                case R.id.nav_popular:
                    mCurrentListType = AppContants.MovieType.POPULAR;
                    mController.loadingPage(AppContants.MovieType.POPULAR);
                    menuItem.setChecked(true);
                    closeDrawer();
                    break;
                case R.id.nav_upcoming:
                    mCurrentListType = AppContants.MovieType.UPCOMMING;
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
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            mSearchView = (SearchView) searchItem.getActionView();
            mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    Log.d("dong.nd1","End");
                    endSearch();
                    return false;
                }
            });
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
                public boolean onQueryTextChange(final String newText) {
                    if(!NetworkUntil.isConnectedToNetwork(getApplication())){
                        mSearchController.setSearchText(null);
                        NetworkUntil.showToastErrorNetwork(getApplication());
                        return true;
                    }
                    mSearchController.setSearchText(newText);
                    if(newText.equals(null) || newText.equals("")){
                        return true;
                    }

                    if (mSearchController != null) {
                        mSearchHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mSearchController.search(newText);
                            }
                        }, SearchController.QUERY_UPDATE_TIME);
                    }

                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {

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


    private void startSearch() {
        SearchFragment searchFragment = new SearchFragment();
        if (mSearchController == null) {
            mSearchController = ViewModelProviders.of(this).get(SearchController.class);
        }
        if (mSearchHandler == null) {
            mSearchHandler = new Handler();
        }
        searchFragment.setArguments(mSearchController);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, searchFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void endSearch() {
        onBackPressed();
    }


}