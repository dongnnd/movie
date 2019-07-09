package com.example.movie.database;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.example.movie.database.dao.MovieDao;
import com.example.movie.model.Movie;
import com.example.movie.network.IVolleyResultCallBack;
import com.example.movie.network.NetworkUntil;
import com.example.movie.uitil.AppContants;
import com.example.movie.uitil.ThreadExecutor;
import com.example.movie.viewmodel.IDataCallBack;

import org.json.JSONArray;

import java.util.List;
import java.util.concurrent.Callable;

public class MovieRepository {

    public static MovieRepository mIntances;
    private MovieDao mMovieDao;

    public MovieRepository(MovieDao movieDao){
        mMovieDao = movieDao;
    }

    public static MovieRepository getInstance(MovieDao  movieDao){
        if(mIntances == null){
            mIntances = new MovieRepository(movieDao);
        }

        return mIntances;
    }

    public void loadListMovie(final int movieType, int section, final IDataCallBack callBack, Context context){
        if(section == -1){
            IVolleyResultCallBack volleyCallBack = new IVolleyResultCallBack() {
                @Override
                public void loadSuccess(final JSONArray jsonArray) {
                    Callable parseJson = new Callable() {
                        @Override
                        public Object call() throws Exception {
                           List<Movie> listMovie = NetworkUntil.parseJson(jsonArray, movieType);
                           if(listMovie != null){
                               callBack.loadSuccess(listMovie);
                           }else{
                               callBack.loadFail(AppContants.VooleyError.ParseError);
                           }
                           return null;
                        }
                    };
                    ThreadExecutor.getInstance().addTaskLoadMovie(parseJson);
                }

                @Override
                public void loadFail(int errorType) {
                    callBack.loadFail(errorType);
                }
            };

            NetworkUntil.getMovieFromServer(movieType, context, volleyCallBack);
        }else{
            List<Movie> movieList = mMovieDao.getListMovieType(1);
            if(movieList != null){
                callBack.loadSuccess(movieList);
            }else{
                callBack.loadFail(-1);
            }
        }
    }

    public void insertListMovie(List<Movie> movieList){
        mMovieDao.insertListPlan(movieList);
    }

    public void deleteListMovie(int type){
        mMovieDao.deleteListMovieType(type);
    }

    public Movie getMovieWithId(int id){
        return mMovieDao.getMovieWithId(id);
    }
}
