package com.example.movie.view.fragment;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.movie.R;
import com.example.movie.model.Movie;
import com.example.movie.viewmodel.FileListController;
import com.example.movie.viewmodel.MainController;

import java.util.List;

public class FileListFragment extends Fragment {

    private MainController mController;
    private FileListAdapter mAdapter;
    private RecyclerView mListView;
    private View rootView;
    private static final int COLUMN_GRID = 2;
    private ProgressBar mProcessBar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mController = new MainController(getActivity().getApplication());
        observerListMovie();
    }

    private void observerListMovie(){
        mController.getListMovie().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mProcessBar.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                mAdapter.updateList(movies);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.file_list_fragment, container, false);
        initRecyclerView();
        return rootView;
    }

    private void initRecyclerView(){
        mListView = rootView.findViewById(R.id.list_view);
        mAdapter = new FileListAdapter(getContext());
        mListView.setLayoutManager(new GridLayoutManager(getContext(), COLUMN_GRID));
        mAdapter = new FileListAdapter(getContext());
        mListView.setAdapter(mAdapter);
        mProcessBar = rootView.findViewById(R.id.progress_bar);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}
