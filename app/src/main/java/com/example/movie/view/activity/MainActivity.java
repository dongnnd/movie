package com.example.movie.view.activity;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.movie.R;
import com.example.movie.uitil.AppContants;
import com.example.movie.view.fragment.FileListFragment;
import com.example.movie.viewmodel.MainController;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavView;

    private MainController mController;


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
        mNavView.setNavigationItemSelectedListener(navListener);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();


    }

    @Override
    public void onBackPressed() {
        if(closeDrawer()) return;
        super.onBackPressed();

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
                    closeDrawer();
                    break;
                case R.id.nav_popular:
                    mController.loadingPage(AppContants.MovieType.POPULAR);
                    closeDrawer();
                    break;
                case R.id.nav_upcoming:
                    mController.loadingPage(AppContants.MovieType.UPCOMMING);
                    closeDrawer();
                    break;

            }
            return false;
        }
    };
}
