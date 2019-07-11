package com.example.movie.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.movie.R;
import com.example.movie.model.Movie;
import com.example.movie.uitil.ThumbnailMrg;
import com.example.movie.view.activity.DetailActivity;

public class DetailFragment extends Fragment {

    public static final String DETAIL_FRAGMENT_TAG = "detail_fragment_tag";
    public static final String ARG_MOVIE = "arg_movie";

    private Movie mMovie;
    private View mRootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovie = (Movie) getArguments().getSerializable(ARG_MOVIE);
        if(mMovie == null){
            Log.d("dong.nd1", "NULL");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout)
                activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null && activity instanceof DetailActivity) {
            appBarLayout.setTitle(mMovie.getTitle());
        }

        ImageView movieBackdrop = ((ImageView) activity.findViewById(R.id.movie_backdrop));
        ThumbnailMrg.getInstance(getContext()).loadThumbnail(mMovie.getIdMovie(), mMovie.getBackdropPath(), movieBackdrop);
    }

    private void loadPoster(){
        ImageView poster = mRootView.findViewById(R.id.movie_poster);
        ThumbnailMrg.getInstance(getContext()).loadThumbnail(mMovie.getIdMovie(), mMovie.getPosterPath(), poster);
    }

    private void loadDetailInfo(){
        TextView tvTitle = mRootView.findViewById(R.id.movie_title);
        tvTitle.setText(mMovie.getTitle());
        TextView tvUserRating = mRootView.findViewById(R.id.movie_user_rating);
        tvUserRating.setText("TMDb: "+mMovie.getVoteAverage() + "/10");
        RatingBar ratingBar = mRootView.findViewById(R.id.movie_rate);
        ratingBar.setRating(mMovie.getVoteAverage() %5);
        TextView tvReleaseData = mRootView.findViewById(R.id.movie_release_date);
        tvReleaseData.setText(mMovie.getReleaseDate());
        TextView tvOverView = mRootView.findViewById(R.id.movie_overview);
        tvOverView.setText(mMovie.getOverview());
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.detail_fragment, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadPoster();
        loadDetailInfo();
    }
}