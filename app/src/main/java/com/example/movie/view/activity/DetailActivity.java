package com.example.movie.view.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.example.movie.R;
import com.example.movie.model.Movie;
import com.example.movie.view.fragment.DetailFragment;
import com.example.movie.view.fragment.FileListFragment;

public class DetailActivity extends AppCompatActivity {

    public static String ARG_MOVIE = "arg_movie";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail);
        initDetailFragment();
    }

    private void initDetailFragment(){
        DetailFragment detail = new DetailFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DetailFragment.ARG_MOVIE, getIntent().getExtras().getSerializable(ARG_MOVIE));
        detail.setArguments(bundle);
        transaction.replace(R.id.movie_detail_container, detail, DetailFragment.DETAIL_FRAGMENT_TAG);
        transaction.addToBackStack(DetailFragment.DETAIL_FRAGMENT_TAG);
        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}