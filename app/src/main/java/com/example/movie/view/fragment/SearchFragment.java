package com.example.movie.view.fragment;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.movie.R;
import com.example.movie.model.Movie;
import com.example.movie.network.NetworkUntil;
import com.example.movie.viewmodel.SearchController;

import java.util.List;

public class SearchFragment extends Fragment {

    public static final String SEARCH_TAG = "search_tag";

    private View mRootView;
    private SearchController mController;
    private SearchAdapter mAdapter;
    private RecyclerView mListItem;
    private LinearLayout mProcessBar;
    private TextView mEmptyView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        observerSearchList();
        observerSearchText();
        observerSearchErrorType();
    }

    private void observerSearchList(){
        mController.getSearchList().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if(movies != null){
                    mProcessBar.setVisibility(View.GONE);
                    mListItem.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                    mAdapter.updateSearchList(movies);
                }else{
                    mProcessBar.setVisibility(View.GONE);
                    mListItem.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private void observerSearchText(){
        mController.getSearchText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if(s == null || s.equals("")){
                    mProcessBar.setVisibility(View.GONE);
                    mListItem.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                    mAdapter.updateSearchList(null);
                    return;
                }
                mProcessBar.setVisibility(View.VISIBLE);
                mListItem.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.GONE);
            }
        });
    }

    private void observerSearchErrorType(){
        mController.getSearchErrorType().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                String errorText = "";
                if (integer == 0){
                    errorText = getContext().getString(R.string.empty_result_search);
                }else{
                    errorText = NetworkUntil.getVolleyErrorMessage(integer);
                }
                mEmptyView.setText(errorText);
            }
        });
    }

    public void setArguments(SearchController controller) {
        mController = controller;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.search_fragment, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initView();
        super.onActivityCreated(savedInstanceState);
    }

    private void initView(){
        mListItem = mRootView.findViewById(R.id.search_list);
        mAdapter = new SearchAdapter(getContext());
        mListItem.setAdapter(mAdapter);
        mListItem.setLayoutManager(new LinearLayoutManager(getContext()));
        mProcessBar = mRootView.findViewById(R.id.process_layout);
        mEmptyView = mRootView.findViewById(R.id.empty_view);
    }


    @Override
    public void onDestroy() {
        mController.refreshData();
        super.onDestroy();
    }
}