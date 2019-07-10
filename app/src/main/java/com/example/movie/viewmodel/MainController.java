package com.example.movie.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.example.movie.database.AppDatabase;
import com.example.movie.database.MovieRepository;
import com.example.movie.model.Movie;
import com.example.movie.network.IVolleyResultCallBack;
import com.example.movie.network.NetworkUntil;
import com.example.movie.uitil.AppContants;
import com.example.movie.uitil.ThreadExecutor;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainController extends AndroidViewModel {

    private static final int CORE_POOL_SIZE = 1;
    private Context mContext;
    private MovieRepository mRepository;
    private MutableLiveData<List<Movie>> mListMovie = new MutableLiveData<>();
    private MutableLiveData<Integer> mErrorVollayType = new MutableLiveData<>();

    private int mLoadedPopuler = -1;
    private int mLoadedTopRate = -1;
    private int mLoadedUpcoming = -1;

    public MainController(Application application) {
        super(application);
        mContext = application;
        AppDatabase database = AppDatabase.getInstance(application);
        mRepository = MovieRepository.getInstance(database.getMovieDao());
    }

//    public void loadingPage(final int type) {
//        final ExecutorService executor = Executors.newFixedThreadPool(CORE_POOL_SIZE);
//        IVolleyResultCallBack callBack = new IVolleyResultCallBack() {
//            @Override
//            public void loadSuccess(final JSONArray jsonArray) {
//                Callable<Void> parseJson = new Callable<Void>() {
//                    @Override
//                    public Void call() throws Exception {
//                        List<Movie> listMovie = NetworkUntil.parseJson(jsonArray, type);
//                        mListMovie.postValue(listMovie);
//                        // insert database
//                        return null;
//                    }
//                };
//                executor.submit(parseJson);
//
//
//            }
//
//            @Override
//            public void loadFail(int errorType) {
//                List<Movie> list = new ArrayList<>();
//                mListMovie.postValue(list);
//            }
//        };
//        NetworkUntil.getMovieFromServer(type, mContext, callBack);
//
//    }

    public void loadingMovie(final int type, final int section){
        final IDataCallBack callBack = new IDataCallBack() {
            @Override
            public void loadSuccess(List<Movie> list) {
                mListMovie.postValue(list);
                insertMovie(list, type);
            }

            @Override
            public void loadFail(int errorType) {
                mErrorVollayType.setValue(errorType);
                mListMovie.postValue(null);
            }
        };
        Callable<Void> loadTask = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                mRepository.loadListMovie(type, section, callBack, mContext);
                return null;
            }
        };
        ThreadExecutor.getInstance().addTaskLoadMovie(loadTask);
    }


    public LiveData<List<Movie>> getListMovie(){
        return mListMovie;
    }

    public void setListMovie(List<Movie> listMovie){
        mListMovie.setValue(listMovie);
    }

    public LiveData<Integer> getErrorVolleyType(){
        return mErrorVollayType;
    }

    private void insertMovie(final List<Movie> movieList, final int movieType){
        Callable insertTask = new Callable() {
            @Override
            public Object call() throws Exception {
                mRepository.deleteListMovie(movieType);
                mRepository.insertListMovie(movieList);
                return null;
            }
        };
        ThreadExecutor.getInstance().addTaskLoadMovie(insertTask);
    }

}