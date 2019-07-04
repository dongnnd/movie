package com.example.movie.view.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movie.R;
import com.example.movie.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ItemHolder>{

    private List<Movie> mItem = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;

    public FileListAdapter(Context context){
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.movie_item, viewGroup);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {

    }

    public void updateList(List<Movie> movies){
        mItem = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mItem != null? mItem.size() : 0;
    }

    public class ItemHolder extends RecyclerView.ViewHolder{

        public ItemHolder(View view){
            super(view);
        }
    }
}
