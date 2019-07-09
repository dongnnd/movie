package com.example.movie.view.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.movie.R;
import com.example.movie.model.Movie;
import com.example.movie.uitil.ThumbnailMrg;

import java.util.List;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchItemHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Movie> mList;

    public SearchAdapter(Context context){
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SearchItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.search_item, viewGroup, false);

        return new SearchItemHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull SearchItemHolder searchItemHolder, int i) {
        Movie movie = mList.get(i);
        ThumbnailMrg.getInstance(mContext).loadThumbnail(movie.getIdMovie(), movie.getPosterPath() ,searchItemHolder.itemIcon);
        searchItemHolder.itemTitle.setText(movie.getTitle());
        searchItemHolder.itemRate.setRating(movie.getVoteAverage()/2);
        if(movie.getReleaseDate() != null && !movie.getReleaseDate().equals("")){
            searchItemHolder.itemDate.setText("ReleaseDate: "+movie.getReleaseDate());
        }

    }

    public void updateSearchList(List<Movie> lists){
        mList = lists;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (mList != null ? mList.size():0);
    }

    public class SearchItemHolder extends RecyclerView.ViewHolder{
        private ImageView itemIcon;
        private TextView itemTitle;
        private TextView itemDate;
        private RatingBar itemRate;

        public SearchItemHolder(View view){
            super(view);
            itemIcon = view.findViewById(R.id.search_item_icon);
            itemTitle = view.findViewById(R.id.search_item_title);
            itemDate = view.findViewById(R.id.search_item_date);
            itemRate = view.findViewById(R.id.search_item_rate);
        }
    }

}