package com.example.movie.view.fragment;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.movie.R;
import com.example.movie.model.Movie;
import com.example.movie.network.NetworkUntil;
import com.example.movie.uitil.AppContants;
import com.example.movie.viewmodel.FileListController;
import com.example.movie.viewmodel.MainController;

import java.util.List;

public class FileListFragment extends Fragment {

    private MainController mController;
    private FileListAdapter mAdapter;
    private RecyclerView mListView;
    private View rootView;
    private static final int COLUMN_GRID = 2;
    private LinearLayout mProcessLayout;
    private TextView mEmptyView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onResume() {
        observerListMovie();
        observerErrorVolleyType();
        mController.loadingMovie(AppContants.MovieType.TOP_RATE, -1);
        super.onResume();
    }

    public void setArguments(MainController controller) {
        mController = controller;
    }

    private void observerListMovie(){
        mController.getListMovie().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                Log.d("dong.nd1", "Run here");
                if(movies != null){
                    mProcessLayout.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                    mAdapter.updateList(movies);
                }else {
                    mProcessLayout.setVisibility(View.GONE);
                    mListView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private void observerErrorVolleyType(){
        mController.getErrorVolleyType().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                mEmptyView.setText(NetworkUntil.getVolleyErrorMessage(integer));
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.file_list_fragment, container, false);
        return rootView;
    }

    private void initRecyclerView(){
        mListView = rootView.findViewById(R.id.list_view);
        mAdapter = new FileListAdapter(getContext());
        mListView.setLayoutManager(new GridLayoutManager(getContext(), COLUMN_GRID));
        mAdapter = new FileListAdapter(getContext());
        mListView.setAdapter(mAdapter);
        mProcessLayout = rootView.findViewById(R.id.process_layout);
        mEmptyView = rootView.findViewById(R.id.empty_view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView();
    }


}
