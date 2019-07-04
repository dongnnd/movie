package com.example.movie.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.example.movie.model.Movie;
import com.example.movie.network.IVolleyResultCallBack;
import com.example.movie.network.NetworkUntil;
import com.example.movie.uitil.AppContants;

import org.json.JSONArray;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainController extends AndroidViewModel {

    private static final int CORE_POOL_SIZE = 1;
    private Context mContext;
    private MutableLiveData<List<Movie>> mListMovie = new MutableLiveData<>();

    public MainController(Application application) {
        super(application);
        mContext = application;
    }

    public void loadingPage(final AppContants.MovieType type) {
        final ExecutorService executor = Executors.newFixedThreadPool(CORE_POOL_SIZE);
        IVolleyResultCallBack callBack = new IVolleyResultCallBack() {
            @Override
            public void loadSuccess(final JSONArray jsonArray) {
                Callable<Void> parseJson = new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        List<Movie> listMovie = NetworkUntil.parseJson(jsonArray);
                        mListMovie.postValue(listMovie);
                        // insert database
                        return null;
                    }
                };
                executor.submit(parseJson);


            }

            @Override
            public void loadFail(String error) {
                Log.d("dong.nd1", "Load fail: " + error);
            }
        };
        NetworkUntil.getMovieFromServer(type, mContext, callBack);

    }


    public LiveData<List<Movie>> getListMovie(){
        return mListMovie;
    }

}
