package com.example.movie.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.IInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.movie.R;
import com.example.movie.model.Movie;
import com.example.movie.uitil.ThumbnailMrg;


import java.util.ArrayList;
import java.util.List;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ItemHolder> {

    private List<Movie> mItem = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;
    private int mItemWidth, mItemHeight;
    private IItemClick mItemClick;

    public FileListAdapter(Context context, IItemClick itemClick) {
        mContext = context;
        mItemClick = itemClick;
        mInflater = LayoutInflater.from(context);

        // init display metrics
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int paddingBase = mContext.getResources().getDimensionPixelOffset(R.dimen.movie_item_base_padding);
        mItemWidth = (displaymetrics.widthPixels - paddingBase * 4) / 2;
        mItemHeight = (int) (mItemWidth * 1.5);

    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.movie_item, viewGroup, false);

        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {
        final Movie movie = mItem.get(i);
        itemHolder.tvName.setText(movie.getTitle());

        itemHolder.imgIcon.getLayoutParams().width = mItemWidth;
        itemHolder.imgIcon.getLayoutParams().height = mItemHeight;

        // score
        int score = movie.getVoteCount()%6;
        for (int n = 0; n < score; n++) {
            itemHolder.scoreID[n].setColorFilter(Color.RED);
        }
        if(movie.getPosterPath() == null){
            Log.d("dong.nd1", "NULL");
        }
        //if(i == 0){
            ThumbnailMrg.getInstance(mContext).loadThumbnail(movie.getIdMovie(), movie.getPosterPath(), itemHolder.imgIcon);
        //}


        itemHolder.itemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClick.onItemClick(movie);
            }
        });
    }

    public void updateList(List<Movie> movies) {
        mItem = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mItem != null ? mItem.size() : 0;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private ImageView imgIcon;
        private TextView tvName;
        private ImageView[] scoreID;
        private LinearLayout itemContainer;

        public ItemHolder(View view) {
            super(view);
            imgIcon = view.findViewById(R.id.item_icon);
            tvName = view.findViewById(R.id.item_name);
            itemContainer = view.findViewById(R.id.item_container);
            addScoreId();
        }

        public void addScoreId() {
            scoreID = new ImageView[5];
            scoreID[0] = itemView.findViewById(R.id.item_score_1);
            scoreID[1] = itemView.findViewById(R.id.item_score_2);
            scoreID[2] = itemView.findViewById(R.id.item_score_3);
            scoreID[3] = itemView.findViewById(R.id.item_score_4);
            scoreID[4] = itemView.findViewById(R.id.item_score_5);
        }
    }

    interface IItemClick{
        void onItemClick(Movie movie);
    }
}
